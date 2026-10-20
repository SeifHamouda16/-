package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.EraAudioManager
import com.example.data.CivilizationData
import com.example.data.UserProgressManager
import com.example.data.local.AppDatabase
import com.example.data.local.EarnedBadgeEntity
import com.example.data.local.EarnedBadgeRepository
import com.example.model.CivilizationSection
import com.example.model.CurrentAudioState
import com.example.model.HistoricalEra
import com.example.model.PharaonicBadge
import com.example.model.QuizQuestion
import com.example.model.ScreenType
import com.example.model.SectionQuizResult
import com.example.data.remote.CloudBroadcastUpdate
import com.example.data.remote.FirebaseProgressRepository
import com.example.sync.AppUpdateManager
import com.example.sync.UpdateInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UserQuestionAnswer(
    val question: QuizQuestion,
    val selectedIndex: Int,
    val isCorrect: Boolean
)

data class UiState(
    val currentScreen: ScreenType = ScreenType.HOME,
    val selectedSection: CivilizationSection? = null,
    val isGrandExam: Boolean = false,
    val searchQuery: String = "",
    val showOnlyBookmarked: Boolean = false,
    // Active Quiz State
    val currentQuizQuestions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isOptionConfirmed: Boolean = false,
    val userAnswers: List<UserQuestionAnswer> = emptyList(),
    val latestResult: SectionQuizResult? = null,
    val newlyUnlockedBadge: PharaonicBadge? = null,
    val showRewardDialog: Boolean = false,
    // Hieroglyphs State
    val hieroglyphicInput: String = "مصر أم الدنيا",
    val hieroglyphicResult: String = "",
    // Audio Narration State
    val selectedAudioEra: HistoricalEra? = null,
    val showAudioPlayerDialog: Boolean = false,
    // Era Detail Screen State
    val selectedEra: HistoricalEra? = null
)

class CivilizationViewModel(application: Application) : AndroidViewModel(application) {

    val progressManager = UserProgressManager(application.applicationContext)

    // Room Database & Repository
    private val database = AppDatabase.getDatabase(application.applicationContext)
    val earnedBadgeRepository = EarnedBadgeRepository(database.earnedBadgeDao())

    val roomEarnedBadges: StateFlow<List<EarnedBadgeEntity>> = earnedBadgeRepository.allEarnedBadges
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(
        UiState(
            hieroglyphicResult = CivilizationData.translateToHieroglyphs("مصر أم الدنيا")
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val navigationBackStack = mutableListOf<ScreenType>()

    val completedSections = progressManager.completedSections
    val quizScores = progressManager.quizScores
    val unlockedBadges = progressManager.unlockedBadges
    val bookmarkedSections = progressManager.bookmarkedSections
    val currentLanguage = progressManager.currentLanguage
    val themeMode = progressManager.themeMode
    val royalThemeColor = progressManager.royalThemeColor
    val userProfile = progressManager.userProfile

    private val audioManager = EraAudioManager(application.applicationContext)
    val audioState: StateFlow<CurrentAudioState> = audioManager.audioState

    val updateManager = AppUpdateManager(application.applicationContext)
    val updateState: StateFlow<UpdateInfo> = updateManager.updateState

    fun setLanguage(language: com.example.model.AppLanguage) {
        progressManager.setLanguage(language)
    }

    fun setThemeMode(mode: com.example.model.ThemeMode) {
        progressManager.setThemeMode(mode)
    }

    fun toggleThemeMode() {
        progressManager.toggleThemeMode()
    }

    fun setRoyalThemeColor(color: com.example.model.RoyalThemeColor) {
        progressManager.setRoyalThemeColor(color)
        val isAr = progressManager.currentLanguage.value == com.example.model.AppLanguage.ARABIC
        val msg = if (isAr) "تم التبديل إلى: ${color.titleAr} ${color.iconEmoji}" else "Switched to: ${color.titleEn} ${color.iconEmoji}"
        android.widget.Toast.makeText(getApplication(), msg, android.widget.Toast.LENGTH_SHORT).show()
    }

    fun cycleRoyalThemeColor(): com.example.model.RoyalThemeColor {
        val next = progressManager.cycleRoyalThemeColor()
        val isAr = progressManager.currentLanguage.value == com.example.model.AppLanguage.ARABIC
        val msg = if (isAr) "تم التبديل إلى: ${next.titleAr} ${next.iconEmoji}" else "Switched to: ${next.titleEn} ${next.iconEmoji}"
        android.widget.Toast.makeText(getApplication(), msg, android.widget.Toast.LENGTH_SHORT).show()
        return next
    }

    fun login(name: String, email: String, avatar: String) {
        progressManager.saveUserProfile(name, email, avatar)
    }

    fun logout() {
        progressManager.logoutUser()
    }

    fun syncData() {
        progressManager.syncData()
    }

    val firebaseRepo = FirebaseProgressRepository(application.applicationContext)

    private val _isCloudSyncing = MutableStateFlow(false)
    val isCloudSyncing: StateFlow<Boolean> = _isCloudSyncing.asStateFlow()

    private val _cloudSyncMessage = MutableStateFlow<String?>(null)
    val cloudSyncMessage: StateFlow<String?> = _cloudSyncMessage.asStateFlow()

    private val _cloudBroadcast = MutableStateFlow<CloudBroadcastUpdate?>(null)
    val cloudBroadcast: StateFlow<CloudBroadcastUpdate?> = _cloudBroadcast.asStateFlow()

    init {
        // Observe real-time cloud updates from Firestore across devices
        viewModelScope.launch {
            firebaseRepo.observeCloudBroadcast().collect { broadcast ->
                _cloudBroadcast.value = broadcast
            }
        }
    }

    /**
     * Upload current device progress to Firebase Firestore immediately.
     */
    fun uploadToCloudNow(onComplete: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isCloudSyncing.value = true
            _cloudSyncMessage.value = "جاري رفع ومزامنة البيانات مع سحابة الفراعنة (Firestore)..."
            val titlePair = progressManager.getUserPharaohTitle()
            val totalPts = progressManager.getTotalPoints()
            val result = firebaseRepo.uploadAllProgressToCloud(
                profile = userProfile.value,
                completedSections = completedSections.value,
                quizScores = quizScores.value,
                badges = roomEarnedBadges.value,
                totalPoints = totalPts,
                pharaohTitle = titlePair.first
            )
            _isCloudSyncing.value = false
            if (result.isSuccess) {
                progressManager.syncData()
                val msg = "تم رفع وحفظ كافة بياناتك على السحابة بنجاح! ☁️"
                _cloudSyncMessage.value = msg
                onComplete(true, msg)
            } else {
                val err = result.exceptionOrNull()?.localizedMessage ?: "فشل الرفع للسحابة"
                _cloudSyncMessage.value = "خطأ في الرفع: $err"
                onComplete(false, err)
            }
        }
    }

    /**
     * Pull and download cloud progress into this device.
     */
    fun downloadFromCloudNow(onComplete: (Boolean, String) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isCloudSyncing.value = true
            _cloudSyncMessage.value = "جاري سحب وتحديث البيانات من السحابة..."
            val result = firebaseRepo.downloadProgressFromCloud()
            _isCloudSyncing.value = false
            _cloudSyncMessage.value = result.message
            if (result.success) {
                progressManager.applyCloudSyncData(
                    profile = result.downloadedProfile,
                    completed = result.completedSections,
                    scores = result.quizScores,
                    badges = result.unlockedBadges
                )
                // Persist downloaded badges into Room database
                result.unlockedBadges.forEach { badgeId ->
                    val b = CivilizationData.badges.find { it.id == badgeId }
                    if (b != null) {
                        earnedBadgeRepository.saveEarnedBadge(
                            badge = b,
                            score = 5,
                            totalQuestions = 5,
                            percentage = 100
                        )
                    }
                }
                onComplete(true, result.message)
            } else {
                onComplete(false, result.message)
            }
        }
    }

    fun checkForAppUpdates(customUrl: String? = null) {
        viewModelScope.launch {
            updateManager.checkForUpdates(customUrl)
        }
    }

    fun downloadAndInstallUpdate(apkUrl: String) {
        viewModelScope.launch {
            updateManager.downloadAndInstallApk(apkUrl)
        }
    }



    val filteredSections: StateFlow<List<CivilizationSection>> = combine(
        _uiState,
        bookmarkedSections
    ) { state, bookmarks ->
        CivilizationData.sections.filter { section ->
            val matchesQuery = state.searchQuery.isBlank() ||
                    section.title.contains(state.searchQuery, ignoreCase = true) ||
                    section.subtitle.contains(state.searchQuery, ignoreCase = true) ||
                    section.summary.contains(state.searchQuery, ignoreCase = true) ||
                    section.subTopics.any { it.title.contains(state.searchQuery, ignoreCase = true) }

            val matchesBookmark = !state.showOnlyBookmarked || bookmarks.contains(section.id)

            matchesQuery && matchesBookmark
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CivilizationData.sections)

    fun navigateTo(screen: ScreenType) {
        if (_uiState.value.currentScreen != screen) {
            navigationBackStack.add(_uiState.value.currentScreen)
            _uiState.value = _uiState.value.copy(currentScreen = screen)
        }
    }

    fun navigateBack(): Boolean {
        if (navigationBackStack.isNotEmpty()) {
            val previousScreen = navigationBackStack.removeAt(navigationBackStack.size - 1)
            _uiState.value = _uiState.value.copy(currentScreen = previousScreen)
            return true
        } else if (_uiState.value.currentScreen != ScreenType.HOME) {
            _uiState.value = _uiState.value.copy(currentScreen = ScreenType.HOME)
            return true
        }
        return false
    }

    fun openSectionDetail(section: CivilizationSection) {
        navigationBackStack.add(_uiState.value.currentScreen)
        _uiState.value = _uiState.value.copy(
            selectedSection = section,
            currentScreen = ScreenType.SECTION_DETAIL
        )
    }

    fun openEraDetail(era: HistoricalEra) {
        navigationBackStack.add(_uiState.value.currentScreen)
        _uiState.value = _uiState.value.copy(
            selectedEra = era,
            currentScreen = ScreenType.ERA_DETAIL
        )
    }

    fun selectEra(era: HistoricalEra) {
        _uiState.value = _uiState.value.copy(
            selectedEra = era
        )
    }

    fun markCurrentSectionRead() {
        val section = _uiState.value.selectedSection ?: return
        progressManager.markSectionCompleted(section.id)
    }

    fun toggleSectionBookmark(sectionId: String) {
        progressManager.toggleBookmark(sectionId)
    }

    fun startSectionQuiz(section: CivilizationSection) {
        navigationBackStack.add(_uiState.value.currentScreen)
        _uiState.value = _uiState.value.copy(
            selectedSection = section,
            isGrandExam = false,
            currentQuizQuestions = section.quiz,
            currentQuestionIndex = 0,
            selectedOptionIndex = null,
            isOptionConfirmed = false,
            userAnswers = emptyList(),
            newlyUnlockedBadge = null,
            showRewardDialog = false,
            currentScreen = ScreenType.QUIZ
        )
    }

    fun startGrandExam() {
        val questions = CivilizationData.grandMasterQuiz.take(10)
        navigationBackStack.add(_uiState.value.currentScreen)
        _uiState.value = _uiState.value.copy(
            selectedSection = null,
            isGrandExam = true,
            currentQuizQuestions = questions,
            currentQuestionIndex = 0,
            selectedOptionIndex = null,
            isOptionConfirmed = false,
            userAnswers = emptyList(),
            newlyUnlockedBadge = null,
            showRewardDialog = false,
            currentScreen = ScreenType.QUIZ
        )
    }

    fun selectQuizOption(optionIndex: Int) {
        if (!_uiState.value.isOptionConfirmed) {
            _uiState.value = _uiState.value.copy(selectedOptionIndex = optionIndex)
        }
    }

    fun confirmSelectedOption() {
        val state = _uiState.value
        val selectedIdx = state.selectedOptionIndex ?: return
        if (state.isOptionConfirmed) return

        val currentQ = state.currentQuizQuestions.getOrNull(state.currentQuestionIndex) ?: return
        val isCorrect = selectedIdx == currentQ.correctIndex

        val answer = UserQuestionAnswer(
            question = currentQ,
            selectedIndex = selectedIdx,
            isCorrect = isCorrect
        )

        _uiState.value = state.copy(
            isOptionConfirmed = true,
            userAnswers = state.userAnswers + answer
        )
    }

    fun nextQuestionOrFinish() {
        val state = _uiState.value
        val nextIdx = state.currentQuestionIndex + 1

        if (nextIdx < state.currentQuizQuestions.size) {
            _uiState.value = state.copy(
                currentQuestionIndex = nextIdx,
                selectedOptionIndex = null,
                isOptionConfirmed = false
            )
        } else {
            // Finish Quiz
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val state = _uiState.value
        val total = state.currentQuizQuestions.size
        val correctCount = state.userAnswers.count { it.isCorrect }
        val sectionId = if (state.isGrandExam) "grand_master" else (state.selectedSection?.id ?: "unknown")

        // Also mark reading section completed when taking quiz
        state.selectedSection?.let {
            progressManager.markSectionCompleted(it.id)
        }

        val result = progressManager.saveQuizResult(sectionId, correctCount, total)

        var newBadge: PharaonicBadge? = null
        if (result.passed) {
            val badge = CivilizationData.badges.find { it.requiredSectionId == sectionId }
            if (badge != null && result.percentage >= badge.minimumScorePercent) {
                newBadge = badge
                // Persist earned badge into Room Database via Repository
                viewModelScope.launch {
                    earnedBadgeRepository.saveEarnedBadge(
                        badge = badge,
                        score = correctCount,
                        totalQuestions = total,
                        percentage = result.percentage
                    )
                }
            }
        }

        _uiState.value = state.copy(
            latestResult = result,
            newlyUnlockedBadge = newBadge,
            showRewardDialog = newBadge != null,
            currentScreen = ScreenType.QUIZ_RESULT
        )

        // Seamless auto-sync to Firebase cloud if authenticated
        if (firebaseRepo.currentUser != null) {
            viewModelScope.launch {
                val titlePair = progressManager.getUserPharaohTitle()
                firebaseRepo.uploadAllProgressToCloud(
                    profile = userProfile.value,
                    completedSections = completedSections.value,
                    quizScores = quizScores.value,
                    badges = roomEarnedBadges.value,
                    totalPoints = progressManager.getTotalPoints(),
                    pharaohTitle = titlePair.first
                )
            }
        }
    }

    fun dismissRewardDialog() {
        _uiState.value = _uiState.value.copy(showRewardDialog = false)
    }

    fun viewRewardInHallOfFame() {
        _uiState.value = _uiState.value.copy(showRewardDialog = false)
        navigateTo(ScreenType.ACHIEVEMENTS)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleBookmarkFilter() {
        _uiState.value = _uiState.value.copy(showOnlyBookmarked = !_uiState.value.showOnlyBookmarked)
    }

    fun updateHieroglyphicInput(text: String) {
        val translated = CivilizationData.translateToHieroglyphs(text)
        _uiState.value = _uiState.value.copy(
            hieroglyphicInput = text,
            hieroglyphicResult = translated
        )
    }

    // Audio Guide Operations
    fun playEraAudio(era: HistoricalEra) {
        audioManager.playEra(era, currentLanguage.value)
        _uiState.value = _uiState.value.copy(
            selectedAudioEra = era,
            showAudioPlayerDialog = true
        )
    }

    fun openAudioPlayerDialog(era: HistoricalEra? = null) {
        val target = era ?: audioState.value.era ?: return
        _uiState.value = _uiState.value.copy(
            selectedAudioEra = target,
            showAudioPlayerDialog = true
        )
    }

    fun dismissAudioPlayerDialog() {
        _uiState.value = _uiState.value.copy(showAudioPlayerDialog = false)
    }

    fun toggleEraAudioPlayback() {
        val current = audioState.value
        if (current.state == com.example.model.AudioPlaybackState.PLAYING) {
            audioManager.pause()
        } else if (current.state == com.example.model.AudioPlaybackState.PAUSED) {
            audioManager.resume()
        } else if (current.era != null) {
            audioManager.playEra(current.era, currentLanguage.value)
        }
    }

    fun seekEraAudio(seconds: Int) {
        audioManager.seekTo(seconds)
    }

    fun setAudioSpeed(speed: Float) {
        audioManager.setPlaybackSpeed(speed)
    }

    fun toggleAudioMute() {
        audioManager.toggleMute()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
