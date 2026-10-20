package com.example.model

data class EraHistoricalMilestone(
    val yearAr: String,
    val yearEn: String,
    val titleAr: String,
    val titleEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val iconEmoji: String = "📜"
)

data class EraProminentFigure(
    val nameAr: String,
    val nameEn: String,
    val roleAr: String,
    val roleEn: String,
    val achievementsAr: String,
    val achievementsEn: String,
    val avatarEmoji: String = "👑"
)

data class EraCivilizationAchievement(
    val titleAr: String,
    val titleEn: String,
    val detailAr: String,
    val detailEn: String,
    val iconEmoji: String = "⚡"
)

data class HistoricalEra(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val periodAr: String,
    val periodEn: String,
    val iconEmoji: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val sectionIds: List<String>,
    val audioNarration: EraAudioNarration? = null,
    val landmarksAr: List<String> = emptyList(),
    val landmarksEn: List<String> = emptyList(),
    val milestones: List<EraHistoricalMilestone> = emptyList(),
    val prominentFigures: List<EraProminentFigure> = emptyList(),
    val achievements: List<EraCivilizationAchievement> = emptyList(),
    val capitalCityAr: String = "",
    val capitalCityEn: String = "",
    val dynastiesAr: String = "",
    val dynastiesEn: String = ""
)

data class EraAudioNarration(
    val titleAr: String,
    val titleEn: String,
    val narratorScriptAr: String,
    val narratorScriptEn: String,
    val durationSeconds: Int,
    val keyPointsAr: List<String>,
    val keyPointsEn: List<String>
)

data class EraCompletionStatus(
    val era: HistoricalEra,
    val totalSections: Int,
    val completedSectionsCount: Int,
    val passedQuizzesCount: Int,
    val readPercentage: Int,
    val quizPercentage: Int,
    val overallPercentage: Int,
    val isMastered: Boolean,
    val isStarted: Boolean
)

enum class AudioPlaybackState {
    IDLE,
    PLAYING,
    PAUSED,
    COMPLETED
}

data class CurrentAudioState(
    val era: HistoricalEra? = null,
    val state: AudioPlaybackState = AudioPlaybackState.IDLE,
    val currentPositionSeconds: Int = 0,
    val totalDurationSeconds: Int = 0,
    val playbackSpeed: Float = 1.0f,
    val isMuted: Boolean = false
)
