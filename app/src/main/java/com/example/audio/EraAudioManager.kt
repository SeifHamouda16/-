package com.example.audio

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.model.AppLanguage
import com.example.model.AudioPlaybackState
import com.example.model.CurrentAudioState
import com.example.model.HistoricalEra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

class EraAudioManager(private val context: Context) : TextToSpeech.OnInitListener {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var tickerJob: Job? = null

    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var currentLanguage: AppLanguage = AppLanguage.ARABIC

    private val _audioState = MutableStateFlow(CurrentAudioState())
    val audioState: StateFlow<CurrentAudioState> = _audioState.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            tts = null
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            setupTtsLanguage(currentLanguage)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    Handler(Looper.getMainLooper()).post {
                        if (_audioState.value.state != AudioPlaybackState.PLAYING) {
                            _audioState.value = _audioState.value.copy(state = AudioPlaybackState.PLAYING)
                        }
                    }
                }

                override fun onDone(utteranceId: String?) {
                    Handler(Looper.getMainLooper()).post {
                        onPlaybackFinished()
                    }
                }

                override fun onError(utteranceId: String?) {
                    // Fallback to simulated playback progress smoothly
                }
            })
        }
    }

    private fun setupTtsLanguage(language: AppLanguage) {
        if (!isTtsInitialized || tts == null) return
        val targetLocale = if (language == AppLanguage.ARABIC) Locale("ar") else Locale.US
        val result = tts?.setLanguage(targetLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Language not installed in system TTS; fallback to default locale or silent simulation
            tts?.setLanguage(Locale.US)
        }
        tts?.setSpeechRate(_audioState.value.playbackSpeed)
    }

    fun playEra(era: HistoricalEra, language: AppLanguage) {
        val narration = era.audioNarration ?: return
        currentLanguage = language

        // If clicking on same era and it was paused, resume
        if (_audioState.value.era?.id == era.id && _audioState.value.state == AudioPlaybackState.PAUSED) {
            resume()
            return
        }

        stop()

        val totalDuration = narration.durationSeconds
        _audioState.value = CurrentAudioState(
            era = era,
            state = AudioPlaybackState.PLAYING,
            currentPositionSeconds = 0,
            totalDurationSeconds = totalDuration,
            playbackSpeed = _audioState.value.playbackSpeed,
            isMuted = _audioState.value.isMuted
        )

        val script = if (language == AppLanguage.ARABIC) narration.narratorScriptAr else narration.narratorScriptEn

        if (isTtsInitialized && tts != null && !_audioState.value.isMuted) {
            setupTtsLanguage(language)
            val params = android.os.Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "era_narration_${era.id}")
            tts?.speak(script, TextToSpeech.QUEUE_FLUSH, params, "era_narration_${era.id}")
        }

        startProgressTicker()
    }

    fun pause() {
        if (_audioState.value.state != AudioPlaybackState.PLAYING) return
        tts?.stop()
        tickerJob?.cancel()
        _audioState.value = _audioState.value.copy(state = AudioPlaybackState.PAUSED)
    }

    fun resume() {
        val currentEra = _audioState.value.era ?: return
        if (_audioState.value.state != AudioPlaybackState.PAUSED) return

        _audioState.value = _audioState.value.copy(state = AudioPlaybackState.PLAYING)

        val narration = currentEra.audioNarration
        if (narration != null && isTtsInitialized && tts != null && !_audioState.value.isMuted) {
            setupTtsLanguage(currentLanguage)
            val script = if (currentLanguage == AppLanguage.ARABIC) narration.narratorScriptAr else narration.narratorScriptEn
            val params = android.os.Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "era_narration_${currentEra.id}")
            tts?.speak(script, TextToSpeech.QUEUE_FLUSH, params, "era_narration_${currentEra.id}")
        }

        startProgressTicker()
    }

    fun stop() {
        tts?.stop()
        tickerJob?.cancel()
        _audioState.value = _audioState.value.copy(
            state = AudioPlaybackState.IDLE,
            currentPositionSeconds = 0
        )
    }

    fun seekTo(seconds: Int) {
        val total = _audioState.value.totalDurationSeconds
        val target = seconds.coerceIn(0, total)
        _audioState.value = _audioState.value.copy(currentPositionSeconds = target)
        if (target >= total) {
            onPlaybackFinished()
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _audioState.value = _audioState.value.copy(playbackSpeed = speed)
        if (isTtsInitialized && tts != null) {
            tts?.setSpeechRate(speed)
        }
    }

    fun toggleMute() {
        val newMuted = !_audioState.value.isMuted
        _audioState.value = _audioState.value.copy(isMuted = newMuted)
        if (newMuted) {
            tts?.stop()
        } else if (_audioState.value.state == AudioPlaybackState.PLAYING) {
            val currentEra = _audioState.value.era
            val narration = currentEra?.audioNarration
            if (narration != null && isTtsInitialized && tts != null) {
                val script = if (currentLanguage == AppLanguage.ARABIC) narration.narratorScriptAr else narration.narratorScriptEn
                val params = android.os.Bundle()
                tts?.speak(script, TextToSpeech.QUEUE_FLUSH, params, "era_narration_${currentEra.id}")
            }
        }
    }

    private fun startProgressTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            val tickDelayMs = (1000L / _audioState.value.playbackSpeed).toLong().coerceAtLeast(200L)
            while (isActive && _audioState.value.state == AudioPlaybackState.PLAYING) {
                delay(tickDelayMs)
                val nextPos = _audioState.value.currentPositionSeconds + 1
                if (nextPos >= _audioState.value.totalDurationSeconds) {
                    onPlaybackFinished()
                    break
                } else {
                    _audioState.value = _audioState.value.copy(currentPositionSeconds = nextPos)
                }
            }
        }
    }

    private fun onPlaybackFinished() {
        tickerJob?.cancel()
        _audioState.value = _audioState.value.copy(
            state = AudioPlaybackState.COMPLETED,
            currentPositionSeconds = _audioState.value.totalDurationSeconds
        )
    }

    fun release() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
