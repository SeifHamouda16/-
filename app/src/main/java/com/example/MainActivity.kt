package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.model.AudioPlaybackState
import com.example.model.ScreenType
import com.example.model.ThemeMode
import com.example.ui.components.EarnedRewardDialog
import com.example.ui.components.EraAudioPlayerDialog
import com.example.ui.components.EraMiniAudioBar
import com.example.ui.components.PharaohBottomNav
import com.example.ui.screens.AccountSettingsScreen
import com.example.ui.screens.AchievementsScreen
import com.example.ui.screens.GrandExamScreen
import com.example.ui.screens.HieroglyphicsScreen
import com.example.ui.screens.HistoricalEraDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SectionDetailScreen
import com.example.data.HistoricalErasData
import com.example.ui.theme.EgyptianCivilizationTheme
import com.example.viewmodel.CivilizationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CivilizationViewModel = viewModel()
            val currentLanguage by viewModel.currentLanguage.collectAsState()
            val themeMode by viewModel.themeMode.collectAsState()
            val royalThemeColor by viewModel.royalThemeColor.collectAsState()

            val layoutDirection = if (currentLanguage == AppLanguage.ARABIC) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            val isDark = themeMode == ThemeMode.DARK

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                EgyptianCivilizationTheme(darkTheme = isDark, royalColor = royalThemeColor) {
                    CivilizationApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CivilizationApp(
    viewModel: CivilizationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val completedSections by viewModel.completedSections.collectAsState()
    val quizScores by viewModel.quizScores.collectAsState()
    val unlockedBadges by viewModel.unlockedBadges.collectAsState()
    val bookmarkedSections by viewModel.bookmarkedSections.collectAsState()
    val filteredSections by viewModel.filteredSections.collectAsState()
    val roomEarnedBadges by viewModel.roomEarnedBadges.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val royalThemeColor by viewModel.royalThemeColor.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val audioState by viewModel.audioState.collectAsState()
    val updateInfo by viewModel.updateState.collectAsState()
    val isCloudSyncing by viewModel.isCloudSyncing.collectAsState()
    val cloudSyncMessage by viewModel.cloudSyncMessage.collectAsState()
    val isDark = themeMode == ThemeMode.DARK

    val userTitle = viewModel.progressManager.getUserPharaohTitle()
    val totalPoints = viewModel.progressManager.getTotalPoints()

    // 4 clean core tabs in bottom nav (Grand Exam removed per user request)
    val showBottomNav = uiState.currentScreen in listOf(
        ScreenType.HOME,
        ScreenType.HIEROGLYPHS,
        ScreenType.ACHIEVEMENTS,
        ScreenType.ACCOUNT_SETTINGS
    )

    // Reward celebration dialog when a badge is unlocked and saved to Room DB
    if (uiState.showRewardDialog && uiState.newlyUnlockedBadge != null) {
        EarnedRewardDialog(
            badge = uiState.newlyUnlockedBadge!!,
            scorePercentage = uiState.latestResult?.percentage ?: 100,
            onDismiss = { viewModel.dismissRewardDialog() },
            onViewHallOfFame = { viewModel.viewRewardInHallOfFame() }
        )
    }

    // Royal Audio Narration Dialog when playing or inspecting an era's audio
    if (uiState.showAudioPlayerDialog && uiState.selectedAudioEra != null) {
        EraAudioPlayerDialog(
            era = uiState.selectedAudioEra!!,
            audioState = audioState,
            currentLanguage = currentLanguage,
            onPlayPause = { viewModel.toggleEraAudioPlayback() },
            onSeekTo = { viewModel.seekEraAudio(it) },
            onSetSpeed = { viewModel.setAudioSpeed(it) },
            onToggleMute = { viewModel.toggleAudioMute() },
            onDismiss = { viewModel.dismissAudioPlayerDialog() }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                PharaohBottomNav(
                    currentScreen = uiState.currentScreen,
                    currentLanguage = currentLanguage,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                ScreenType.HOME -> {
                    HomeScreen(
                        sections = filteredSections,
                        completedSections = completedSections,
                        quizScores = quizScores,
                        bookmarkedSections = bookmarkedSections,
                        userTitle = userTitle,
                        totalPoints = totalPoints,
                        currentLanguage = currentLanguage,
                        userProfile = userProfile,
                        searchQuery = uiState.searchQuery,
                        showOnlyBookmarked = uiState.showOnlyBookmarked,
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        onToggleBookmarkFilter = { viewModel.toggleBookmarkFilter() },
                        onSectionClick = { section -> viewModel.openSectionDetail(section) },
                        onStartQuizClick = { section -> viewModel.startSectionQuiz(section) },
                        onToggleBookmark = { sectionId -> viewModel.toggleSectionBookmark(sectionId) },
                        onStartGrandExam = { viewModel.startGrandExam() },
                        onOpenAccountSettings = { viewModel.navigateTo(ScreenType.ACCOUNT_SETTINGS) },
                        onToggleTheme = { viewModel.toggleThemeMode() },
                        onCycleThemeColor = { viewModel.cycleRoyalThemeColor() },
                        isDarkTheme = isDark,
                        audioState = audioState,
                        onPlayEraAudio = { era -> viewModel.playEraAudio(era) },
                        onToggleEraAudio = { viewModel.toggleEraAudioPlayback() },
                        onExpandAudioPlayer = { viewModel.openAudioPlayerDialog() },
                        onOpenEraDetail = { era -> viewModel.openEraDetail(era) }
                    )
                }

                ScreenType.ERA_DETAIL -> {
                    val era = uiState.selectedEra ?: HistoricalErasData.eras.first()
                    HistoricalEraDetailScreen(
                        era = era,
                        completedSections = completedSections,
                        quizScores = quizScores,
                        audioState = audioState,
                        currentLanguage = currentLanguage,
                        onNavigateBack = { viewModel.navigateBack() },
                        onSelectEra = { selected -> viewModel.selectEra(selected) },
                        onOpenSection = { section -> viewModel.openSectionDetail(section) },
                        onStartQuiz = { section -> viewModel.startSectionQuiz(section) },
                        onPlayAudio = { targetEra -> viewModel.playEraAudio(targetEra) },
                        onToggleAudioPlayback = { viewModel.toggleEraAudioPlayback() },
                        onSeekAudio = { seconds -> viewModel.seekEraAudio(seconds) },
                        onSetAudioSpeed = { speed -> viewModel.setAudioSpeed(speed) },
                        onToggleAudioMute = { viewModel.toggleAudioMute() },
                        onToggleTheme = { viewModel.toggleThemeMode() },
                        isDarkTheme = isDark
                    )
                }

                ScreenType.SECTION_DETAIL -> {
                    val section = uiState.selectedSection
                    if (section != null) {
                        SectionDetailScreen(
                            section = section,
                            isCompleted = completedSections.contains(section.id),
                            quizResult = quizScores[section.id],
                            isBookmarked = bookmarkedSections.contains(section.id),
                            onNavigateBack = { viewModel.navigateBack() },
                            onMarkCompleted = { viewModel.markCurrentSectionRead() },
                            onToggleBookmark = { viewModel.toggleSectionBookmark(section.id) },
                            onStartQuiz = { viewModel.startSectionQuiz(section) },
                            onToggleTheme = { viewModel.toggleThemeMode() },
                            isDarkTheme = isDark,
                            audioState = audioState,
                            currentLanguage = currentLanguage,
                            onPlayEraAudio = { era -> viewModel.playEraAudio(era) },
                            onToggleAudioPlayback = { viewModel.toggleEraAudioPlayback() },
                            onOpenAudioDialog = { era -> viewModel.openAudioPlayerDialog(era) }
                        )
                    }
                }

                ScreenType.QUIZ -> {
                    QuizScreen(
                        section = uiState.selectedSection,
                        isGrandExam = uiState.isGrandExam,
                        questions = uiState.currentQuizQuestions,
                        currentIndex = uiState.currentQuestionIndex,
                        selectedOption = uiState.selectedOptionIndex,
                        isConfirmed = uiState.isOptionConfirmed,
                        onSelectOption = { viewModel.selectQuizOption(it) },
                        onConfirmAnswer = { viewModel.confirmSelectedOption() },
                        onNextQuestion = { viewModel.nextQuestionOrFinish() },
                        onExitQuiz = { viewModel.navigateBack() }
                    )
                }

                ScreenType.QUIZ_RESULT -> {
                    val result = uiState.latestResult
                    if (result != null) {
                        QuizResultScreen(
                            section = uiState.selectedSection,
                            isGrandExam = uiState.isGrandExam,
                            result = result,
                            userAnswers = uiState.userAnswers,
                            unlockedBadge = uiState.newlyUnlockedBadge,
                            onRetakeQuiz = {
                                val currentSec = uiState.selectedSection
                                if (currentSec != null) {
                                    viewModel.startSectionQuiz(currentSec)
                                } else {
                                    viewModel.startGrandExam()
                                }
                            },
                            onBackToHome = { viewModel.navigateTo(ScreenType.HOME) },
                            onBackToSection = {
                                val currentSec = uiState.selectedSection
                                if (currentSec != null) {
                                    viewModel.openSectionDetail(currentSec)
                                } else {
                                    viewModel.navigateTo(ScreenType.HOME)
                                }
                            }
                        )
                    }
                }

                ScreenType.HIEROGLYPHS -> {
                    HieroglyphicsScreen(
                        inputText = uiState.hieroglyphicInput,
                        translatedText = uiState.hieroglyphicResult,
                        onInputChange = { viewModel.updateHieroglyphicInput(it) },
                        onNavigateBack = { viewModel.navigateBack() },
                        onToggleTheme = { viewModel.toggleThemeMode() },
                        isDarkTheme = isDark
                    )
                }

                ScreenType.ACHIEVEMENTS -> {
                    AchievementsScreen(
                        userTitle = userTitle,
                        totalPoints = totalPoints,
                        unlockedBadges = unlockedBadges,
                        roomEarnedBadges = roomEarnedBadges,
                        quizScores = quizScores,
                        onNavigateBack = { viewModel.navigateBack() },
                        onToggleTheme = { viewModel.toggleThemeMode() },
                        isDarkTheme = isDark
                    )
                }

                ScreenType.GRAND_EXAM -> {
                    GrandExamScreen(
                        lastResult = quizScores["grand_master"],
                        onStartExam = { viewModel.startGrandExam() },
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                }

                ScreenType.ACCOUNT_SETTINGS -> {
                    AccountSettingsScreen(
                        userProfile = userProfile,
                        currentLanguage = currentLanguage,
                        themeMode = themeMode,
                        roomBadges = roomEarnedBadges,
                        userTitle = userTitle,
                        totalPoints = totalPoints,
                        onLogin = { name, email, avatar -> viewModel.login(name, email, avatar) },
                        onLogout = { viewModel.logout() },
                        onSyncData = { viewModel.syncData() },
                        onLanguageChange = { lang -> viewModel.setLanguage(lang) },
                        onThemeChange = { mode -> viewModel.setThemeMode(mode) },
                        royalThemeColor = royalThemeColor,
                        onSelectRoyalColor = { color -> viewModel.setRoyalThemeColor(color) },
                        onCycleRoyalColor = { viewModel.cycleRoyalThemeColor() },
                        onNavigateBack = { viewModel.navigateBack() },
                        updateInfo = updateInfo,
                        onCheckForUpdates = { viewModel.checkForAppUpdates() },
                        onDownloadAndInstall = { url -> viewModel.downloadAndInstallUpdate(url) },
                        isCloudSyncing = isCloudSyncing,
                        cloudSyncMessage = cloudSyncMessage,
                        onUploadToCloud = { viewModel.uploadToCloudNow() },
                        onDownloadFromCloud = { viewModel.downloadFromCloudNow() }
                    )
                }
            }
        }
    }
}
