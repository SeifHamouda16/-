package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.model.AudioPlaybackState
import com.example.model.CurrentAudioState
import com.example.model.HistoricalEra
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

@Composable
fun EraAudioPlayerDialog(
    era: HistoricalEra,
    audioState: CurrentAudioState,
    currentLanguage: AppLanguage,
    onPlayPause: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onToggleMute: () -> Unit,
    onDismiss: () -> Unit
) {
    val narration = era.audioNarration ?: return
    val title = if (currentLanguage == AppLanguage.ARABIC) narration.titleAr else narration.titleEn
    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val script = if (currentLanguage == AppLanguage.ARABIC) narration.narratorScriptAr else narration.narratorScriptEn
    val keyPoints = if (currentLanguage == AppLanguage.ARABIC) narration.keyPointsAr else narration.keyPointsEn

    val isPlaying = audioState.state == AudioPlaybackState.PLAYING
    val isCompleted = audioState.state == AudioPlaybackState.COMPLETED
    val progressRatio = if (audioState.totalDurationSeconds > 0) {
        audioState.currentPositionSeconds.toFloat() / audioState.totalDurationSeconds.toFloat()
    } else 0f

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("era_audio_player_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PharaohGold.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "الدليل الصوتي الملكي" else "Royal Audio Guide",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("audio_close_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = PharaohGold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Emblem + Era Icon
                Surface(
                    shape = CircleShape,
                    color = PharaohGold.copy(alpha = 0.18f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, PharaohGold),
                    modifier = Modifier.size(76.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(era.iconEmoji, fontSize = 38.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "$eraTitle • $eraPeriod",
                    style = MaterialTheme.typography.bodySmall,
                    color = PharaohGoldDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Animated Waveform Visualizer
                AudioWaveformVisualizer(isPlaying = isPlaying)

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Slider
                Slider(
                    value = progressRatio.coerceIn(0f, 1f),
                    onValueChange = { ratio ->
                        val targetSec = (ratio * audioState.totalDurationSeconds).toInt()
                        onSeekTo(targetSec)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = PharaohGold,
                        activeTrackColor = PharaohGold,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                // Time labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(audioState.currentPositionSeconds),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                    Text(
                        text = formatTime(audioState.totalDurationSeconds),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Main Playback Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rewind 10s
                    IconButton(
                        onClick = { onSeekTo(audioState.currentPositionSeconds - 10) },
                        modifier = Modifier.testTag("audio_rewind_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastRewind,
                            contentDescription = "-10 ثوانٍ",
                            tint = PharaohGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Large Play / Pause / Replay Button
                    Surface(
                        shape = CircleShape,
                        color = PharaohGold,
                        border = androidx.compose.foundation.BorderStroke(2.dp, PharaohGoldLight),
                        modifier = Modifier
                            .size(62.dp)
                            .clickable { onPlayPause() }
                            .testTag("audio_play_pause_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when {
                                    isCompleted -> Icons.Default.Replay
                                    isPlaying -> Icons.Default.Pause
                                    else -> Icons.Default.PlayArrow
                                },
                                contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                tint = Color.Black,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    // Forward 10s
                    IconButton(
                        onClick = { onSeekTo(audioState.currentPositionSeconds + 10) },
                        modifier = Modifier.testTag("audio_forward_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "+10 ثوانٍ",
                            tint = PharaohGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Mute / Unmute
                    IconButton(
                        onClick = onToggleMute,
                        modifier = Modifier.testTag("audio_mute_btn")
                    ) {
                        Icon(
                            imageVector = if (audioState.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = if (audioState.isMuted) "كتم الصوت" else "تشغيل الصوت",
                            tint = if (audioState.isMuted) MaterialTheme.colorScheme.error else PharaohGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Narration Speed Selector
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(0.75f, 1.0f, 1.25f, 1.5f).forEach { speed ->
                        val isSelected = audioState.playbackSpeed == speed
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) PharaohGold else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .clickable { onSetSpeed(speed) }
                                .testTag("audio_speed_${speed}x")
                        ) {
                            Text(
                                text = "${speed}x",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Spoken Transcript Section
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "نص الشرح الصوتي" else "Narration Transcript",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = script,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "محاور صوتية رئيسية:" else "Key Spoken Highlights:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGoldDark
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        keyPoints.forEach { point ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("✦", color = PharaohGold, fontSize = 12.sp)
                                Text(
                                    text = point,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EraMiniAudioBar(
    audioState: CurrentAudioState,
    currentLanguage: AppLanguage,
    onPlayPause: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val era = audioState.era ?: return
    val narration = era.audioNarration ?: return
    val isPlaying = audioState.state == AudioPlaybackState.PLAYING
    val isCompleted = audioState.state == AudioPlaybackState.COMPLETED

    val title = if (currentLanguage == AppLanguage.ARABIC) narration.titleAr else narration.titleEn

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onExpand() }
            .testTag("era_mini_audio_bar"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PharaohGold.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(era.iconEmoji, fontSize = 18.sp)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${formatTime(audioState.currentPositionSeconds)} / ${formatTime(audioState.totalDurationSeconds)}",
                            fontSize = 10.sp,
                            color = PharaohGoldDark
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Small waveform
                    if (isPlaying) {
                        AudioMiniWaveform()
                    }

                    // Play / Pause Button
                    Surface(
                        shape = CircleShape,
                        color = PharaohGold,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onPlayPause() }
                            .testTag("mini_audio_play_pause")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when {
                                    isCompleted -> Icons.Default.Replay
                                    isPlaying -> Icons.Default.Pause
                                    else -> Icons.Default.PlayArrow
                                },
                                contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onExpand,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "توسيع المشغل",
                            tint = PharaohGold
                        )
                    }
                }
            }

            // Bottom subtle progress line
            val progress = if (audioState.totalDurationSeconds > 0) {
                audioState.currentPositionSeconds.toFloat() / audioState.totalDurationSeconds.toFloat()
            } else 0f

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp),
                color = PharaohGold,
                trackColor = Color.Transparent
            )
        }
    }
}

@Composable
fun AudioWaveformVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val heights = (0..9).map { index ->
        val duration = 400 + (index * 70)
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = if (isPlaying) 1.0f else 0.25f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "wave_$index"
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(36.dp)
            .padding(vertical = 4.dp)
    ) {
        heights.forEach { animatedHeight ->
            val heightDp = (animatedHeight.value * 30).coerceAtLeast(6f).dp
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(heightDp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(PharaohGoldLight, PharaohGold, PharaohGoldDark)
                        )
                    )
            )
        }
    }
}

@Composable
fun AudioMiniWaveform() {
    val infiniteTransition = rememberInfiniteTransition(label = "mini_waveform")
    val heights = (0..3).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(350 + (index * 80), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "mini_wave_$index"
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(16.dp)
    ) {
        heights.forEach { animatedHeight ->
            val heightDp = (animatedHeight.value * 14).coerceAtLeast(4f).dp
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(heightDp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(PharaohGold)
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
