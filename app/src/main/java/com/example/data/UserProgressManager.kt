package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppLanguage
import com.example.model.SectionQuizResult
import com.example.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProgressManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("pharaoh_progress", Context.MODE_PRIVATE)

    private val _completedSections = MutableStateFlow<Set<String>>(emptySet())
    val completedSections: StateFlow<Set<String>> = _completedSections.asStateFlow()

    private val _quizScores = MutableStateFlow<Map<String, SectionQuizResult>>(emptyMap())
    val quizScores: StateFlow<Map<String, SectionQuizResult>> = _quizScores.asStateFlow()

    private val _unlockedBadges = MutableStateFlow<Set<String>>(emptySet())
    val unlockedBadges: StateFlow<Set<String>> = _unlockedBadges.asStateFlow()

    private val _bookmarkedSections = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedSections: StateFlow<Set<String>> = _bookmarkedSections.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ARABIC)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _themeMode = MutableStateFlow(com.example.model.ThemeMode.DARK)
    val themeMode: StateFlow<com.example.model.ThemeMode> = _themeMode.asStateFlow()

    private val _royalThemeColor = MutableStateFlow(com.example.model.RoyalThemeColor.GOLD)
    val royalThemeColor: StateFlow<com.example.model.RoyalThemeColor> = _royalThemeColor.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _hasSeenWelcome = MutableStateFlow(false)
    val hasSeenWelcome: StateFlow<Boolean> = _hasSeenWelcome.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val completed = prefs.getStringSet("KEY_COMPLETED_SECTIONS", emptySet()) ?: emptySet()
        _completedSections.value = completed

        val bookmarks = prefs.getStringSet("KEY_BOOKMARKS", emptySet()) ?: emptySet()
        _bookmarkedSections.value = bookmarks

        val badges = prefs.getStringSet("KEY_UNLOCKED_BADGES", emptySet()) ?: emptySet()
        _unlockedBadges.value = badges

        // Load language preference
        val langCode = prefs.getString("KEY_APP_LANGUAGE", AppLanguage.ARABIC.code) ?: AppLanguage.ARABIC.code
        _currentLanguage.value = if (langCode == AppLanguage.ENGLISH.code) AppLanguage.ENGLISH else AppLanguage.ARABIC

        // Load theme preference
        val themeCode = prefs.getString("KEY_THEME_MODE", com.example.model.ThemeMode.DARK.code) ?: com.example.model.ThemeMode.DARK.code
        _themeMode.value = if (themeCode == com.example.model.ThemeMode.LIGHT.code) com.example.model.ThemeMode.LIGHT else com.example.model.ThemeMode.DARK

        // Load royal color preference (defaults to GOLD)
        val royalColorCode = prefs.getString("KEY_ROYAL_THEME_COLOR", com.example.model.RoyalThemeColor.GOLD.code)
        val loadedRoyalColor = com.example.model.RoyalThemeColor.fromCode(royalColorCode)
        _royalThemeColor.value = loadedRoyalColor
        com.example.ui.theme.CurrentRoyalThemeState.value = loadedRoyalColor

        // Load welcome onboarding status
        _hasSeenWelcome.value = prefs.getBoolean("KEY_HAS_SEEN_WELCOME_V2", false)

        // Load user profile
        val isLoggedIn = prefs.getBoolean("KEY_USER_LOGGED_IN", false)
        val userName = prefs.getString("KEY_USER_NAME", "باحث الفراعنة") ?: "باحث الفراعنة"
        val userEmail = prefs.getString("KEY_USER_EMAIL", "explorer@egypt.ancient") ?: "explorer@egypt.ancient"
        val userAvatar = prefs.getString("KEY_USER_AVATAR", "👑") ?: "👑"
        val syncTime = prefs.getLong("KEY_LAST_SYNC_TIME", System.currentTimeMillis())

        _userProfile.value = UserProfile(
            isLoggedIn = isLoggedIn,
            name = userName,
            email = userEmail,
            avatarEmoji = userAvatar,
            lastSyncTimestamp = syncTime
        )

        // Load quiz scores
        val scoresMap = mutableMapOf<String, SectionQuizResult>()
        for (section in CivilizationData.sections) {
            val key = "QUIZ_SCORE_${section.id}"
            if (prefs.contains(key)) {
                val score = prefs.getInt("${key}_score", 0)
                val total = prefs.getInt("${key}_total", section.quiz.size)
                val percent = prefs.getInt("${key}_percent", 0)
                val passed = prefs.getBoolean("${key}_passed", false)
                val time = prefs.getLong("${key}_time", 0L)
                scoresMap[section.id] = SectionQuizResult(
                    sectionId = section.id,
                    score = score,
                    totalQuestions = total,
                    percentage = percent,
                    passed = passed,
                    completedAt = time
                )
            }
        }
        // Load grand master quiz score
        if (prefs.contains("QUIZ_SCORE_grand_master")) {
            val score = prefs.getInt("QUIZ_SCORE_grand_master_score", 0)
            val total = prefs.getInt("QUIZ_SCORE_grand_master_total", 10)
            val percent = prefs.getInt("QUIZ_SCORE_grand_master_percent", 0)
            val passed = prefs.getBoolean("QUIZ_SCORE_grand_master_passed", false)
            val time = prefs.getLong("QUIZ_SCORE_grand_master_time", 0L)
            scoresMap["grand_master"] = SectionQuizResult(
                sectionId = "grand_master",
                score = score,
                totalQuestions = total,
                percentage = percent,
                passed = passed,
                completedAt = time
            )
        }
        _quizScores.value = scoresMap
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        prefs.edit().putString("KEY_APP_LANGUAGE", language.code).apply()
    }

    fun setThemeMode(mode: com.example.model.ThemeMode) {
        _themeMode.value = mode
        prefs.edit().putString("KEY_THEME_MODE", mode.code).apply()
    }

    fun toggleThemeMode() {
        val newMode = if (_themeMode.value == com.example.model.ThemeMode.DARK) com.example.model.ThemeMode.LIGHT else com.example.model.ThemeMode.DARK
        setThemeMode(newMode)
    }

    fun setRoyalThemeColor(color: com.example.model.RoyalThemeColor) {
        _royalThemeColor.value = color
        com.example.ui.theme.CurrentRoyalThemeState.value = color
        prefs.edit().putString("KEY_ROYAL_THEME_COLOR", color.code).apply()
    }

    fun cycleRoyalThemeColor(): com.example.model.RoyalThemeColor {
        val all = com.example.model.RoyalThemeColor.values()
        val currentIndex = all.indexOf(_royalThemeColor.value)
        val nextIndex = if (currentIndex == -1) 0 else (currentIndex + 1) % all.size
        val nextColor = all[nextIndex]
        setRoyalThemeColor(nextColor)
        return nextColor
    }

    fun setWelcomeCompleted(completed: Boolean = true) {
        _hasSeenWelcome.value = completed
        prefs.edit().putBoolean("KEY_HAS_SEEN_WELCOME_V2", completed).apply()
    }

    fun saveUserProfile(name: String, email: String, avatarEmoji: String) {
        val now = System.currentTimeMillis()
        val profile = UserProfile(
            isLoggedIn = true,
            name = name.ifBlank { "باحث الفراعنة" },
            email = email.ifBlank { "explorer@egypt.ancient" },
            avatarEmoji = avatarEmoji,
            lastSyncTimestamp = now
        )
        _userProfile.value = profile
        prefs.edit()
            .putBoolean("KEY_USER_LOGGED_IN", true)
            .putString("KEY_USER_NAME", profile.name)
            .putString("KEY_USER_EMAIL", profile.email)
            .putString("KEY_USER_AVATAR", profile.avatarEmoji)
            .putLong("KEY_LAST_SYNC_TIME", now)
            .apply()
    }

    fun logoutUser() {
        val guest = UserProfile(
            isLoggedIn = false,
            name = "زائر طيبة",
            email = "",
            avatarEmoji = "🏺",
            lastSyncTimestamp = System.currentTimeMillis()
        )
        _userProfile.value = guest
        prefs.edit()
            .putBoolean("KEY_USER_LOGGED_IN", false)
            .putString("KEY_USER_NAME", guest.name)
            .putString("KEY_USER_EMAIL", "")
            .putString("KEY_USER_AVATAR", guest.avatarEmoji)
            .apply()
    }

    fun syncData(): Long {
        val now = System.currentTimeMillis()
        _userProfile.value = _userProfile.value.copy(lastSyncTimestamp = now)
        prefs.edit().putLong("KEY_LAST_SYNC_TIME", now).apply()
        return now
    }

    fun applyCloudSyncData(
        profile: UserProfile?,
        completed: Set<String>,
        scores: Map<String, SectionQuizResult>,
        badges: Set<String>
    ) {
        val editor = prefs.edit()
        profile?.let {
            _userProfile.value = it
            editor.putBoolean("KEY_USER_LOGGED_IN", it.isLoggedIn)
                .putString("KEY_USER_NAME", it.name)
                .putString("KEY_USER_EMAIL", it.email)
                .putString("KEY_USER_AVATAR", it.avatarEmoji)
                .putLong("KEY_LAST_SYNC_TIME", it.lastSyncTimestamp)
        }

        if (completed.isNotEmpty()) {
            val merged = _completedSections.value + completed
            _completedSections.value = merged
            editor.putStringSet("KEY_COMPLETED_SECTIONS", merged)
        }

        if (badges.isNotEmpty()) {
            val mergedBadges = _unlockedBadges.value + badges
            _unlockedBadges.value = mergedBadges
            editor.putStringSet("KEY_UNLOCKED_BADGES", mergedBadges)
        }

        if (scores.isNotEmpty()) {
            val currentScores = _quizScores.value.toMutableMap()
            for ((secId, res) in scores) {
                val existing = currentScores[secId]
                if (existing == null || res.score >= existing.score) {
                    currentScores[secId] = res
                    val key = "QUIZ_SCORE_$secId"
                    editor.putInt("${key}_score", res.score)
                        .putInt("${key}_total", res.totalQuestions)
                        .putInt("${key}_percent", res.percentage)
                        .putBoolean("${key}_passed", res.passed)
                        .putLong("${key}_time", res.completedAt)
                }
            }
            _quizScores.value = currentScores
        }

        editor.apply()
    }

    fun markSectionCompleted(sectionId: String) {
        val updated = _completedSections.value.toMutableSet().apply { add(sectionId) }
        _completedSections.value = updated
        prefs.edit().putStringSet("KEY_COMPLETED_SECTIONS", updated).apply()
    }

    fun toggleBookmark(sectionId: String) {
        val current = _bookmarkedSections.value.toMutableSet()
        if (current.contains(sectionId)) {
            current.remove(sectionId)
        } else {
            current.add(sectionId)
        }
        _bookmarkedSections.value = current
        prefs.edit().putStringSet("KEY_BOOKMARKS", current).apply()
    }

    fun saveQuizResult(sectionId: String, score: Int, total: Int): SectionQuizResult {
        val percent = if (total > 0) (score * 100) / total else 0
        val passed = percent >= 70

        val result = SectionQuizResult(
            sectionId = sectionId,
            score = score,
            totalQuestions = total,
            percentage = percent,
            passed = passed,
            completedAt = System.currentTimeMillis()
        )

        val updatedScores = _quizScores.value.toMutableMap()
        val existing = updatedScores[sectionId]
        if (existing == null || score >= existing.score) {
            updatedScores[sectionId] = result
            _quizScores.value = updatedScores

            val key = "QUIZ_SCORE_$sectionId"
            prefs.edit()
                .putInt("${key}_score", score)
                .putInt("${key}_total", total)
                .putInt("${key}_percent", percent)
                .putBoolean("${key}_passed", passed)
                .putLong("${key}_time", result.completedAt)
                .apply()
        }

        // Check badge unlock
        if (passed) {
            val badge = CivilizationData.badges.find { it.requiredSectionId == sectionId }
            if (badge != null && percent >= badge.minimumScorePercent) {
                unlockBadge(badge.id)
            }
        }

        return result
    }

    private fun unlockBadge(badgeId: String) {
        val updated = _unlockedBadges.value.toMutableSet().apply { add(badgeId) }
        _unlockedBadges.value = updated
        prefs.edit().putStringSet("KEY_UNLOCKED_BADGES", updated).apply()
    }

    fun getUserPharaohTitle(): Pair<String, String> {
        val passedCount = _quizScores.value.values.count { it.passed }
        val totalSections = CivilizationData.sections.size

        return when {
            passedCount >= totalSections -> Pair("حكيم الإمبراطورية الأعظم", "أتقنت كافة اختبارات الحضارة ونلت بركة تحوت")
            passedCount >= 4 -> Pair("مستشار الفرعون الملكي", "معرفتك بالآلهة والمعابد والحياة الفرعونية عميقة وراسخة")
            passedCount >= 2 -> Pair("كاهن معبد آمون", "اجتزت عدة امتحانات وتعمقت في أسرار الخلود")
            passedCount >= 1 -> Pair("كاتب البردي المعتمد", "بدأت خطوتك الأولى في تدوين تاريخ الأجداد")
            else -> Pair("زائر طيبة المستكشف", "ابدأ قراءة الأقسام وخوض الاختبارات لترتقي بالرتب الفرعونية")
        }
    }

    fun getTotalPoints(): Int {
        return _quizScores.value.values.sumOf { it.score * 20 }
    }
}
