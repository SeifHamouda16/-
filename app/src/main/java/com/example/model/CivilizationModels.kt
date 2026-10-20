package com.example.model

data class CivilizationSection(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val drawableResId: Int,
    val readTimeMinutes: Int,
    val era: String,
    val summary: String,
    val subTopics: List<SubTopic>,
    val secrets: List<PharaohSecret>,
    val quiz: List<QuizQuestion>
)

data class SubTopic(
    val id: String,
    val title: String,
    val content: String,
    val keyPoints: List<String>,
    val quoteOrInscription: String? = null
)

data class PharaohSecret(
    val title: String,
    val secretText: String,
    val icon: String = "✨"
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val historicalFunFact: String
)

data class SectionQuizResult(
    val sectionId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val passed: Boolean,
    val completedAt: Long
)

data class PharaonicBadge(
    val id: String,
    val title: String,
    val description: String,
    val glyphEmoji: String,
    val requiredSectionId: String,
    val minimumScorePercent: Int = 70
)

data class HieroglyphGlyph(
    val arabicLetter: String,
    val englishLetter: String,
    val hieroglyphSymbol: String,
    val name: String,
    val pronunciation: String,
    val meaning: String
)

enum class ScreenType {
    WELCOME,
    HOME,
    SECTION_DETAIL,
    ERA_DETAIL,
    QUIZ,
    QUIZ_RESULT,
    HIEROGLYPHS,
    ACHIEVEMENTS,
    GRAND_EXAM,
    ACCOUNT_SETTINGS
}

