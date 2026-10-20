package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.CivilizationData
import com.example.data.HistoricalErasData
import com.example.data.Localization
import com.example.data.UserProgressManager
import com.example.data.local.AppDatabase
import com.example.data.local.EarnedBadgeRepository
import com.example.model.AppLanguage
import com.example.model.SectionQuizResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: EarnedBadgeRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = EarnedBadgeRepository(db.earnedBadgeDao())
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("حضارة مصر", appName)
    }

    @Test
    fun `test user progress manager quiz score saving`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val progressManager = UserProgressManager(context)

        val result = progressManager.saveQuizResult("pyramids_architecture", 5, 5)
        assertEquals(100, result.percentage)
        assertTrue(result.passed)

        val title = progressManager.getUserPharaohTitle()
        assertNotNull(title.first)
        assertTrue(progressManager.getTotalPoints() > 0)
    }

    @Test
    fun `test room database badge storage and retrieval`() = runBlocking {
        val badge = CivilizationData.badges.first()
        val savedEntity = repository.saveEarnedBadge(
            badge = badge,
            score = 5,
            totalQuestions = 5,
            percentage = 100
        )

        assertNotNull(savedEntity)
        assertEquals(badge.id, savedEntity.badgeId)
        assertEquals(100, savedEntity.scorePercentage)

        val allBadges = repository.allEarnedBadges.first()
        assertEquals(1, allBadges.size)
        assertEquals(badge.title, allBadges[0].title)

        val hasBadge = repository.hasBadge(badge.id)
        assertTrue(hasBadge)
    }

    @Test
    fun `test user login sync and logout`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val progressManager = UserProgressManager(context)

        progressManager.saveUserProfile("رمسيس المستكشف", "ramesses@ancient.eg", "👑")
        val profile = progressManager.userProfile.value
        assertTrue(profile.isLoggedIn)
        assertEquals("رمسيس المستكشف", profile.name)
        assertEquals("ramesses@ancient.eg", profile.email)

        val syncTime = progressManager.syncData()
        assertTrue(syncTime > 0)

        progressManager.logoutUser()
        assertFalse(progressManager.userProfile.value.isLoggedIn)
    }

    @Test
    fun `test language setting and localization`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val progressManager = UserProgressManager(context)

        progressManager.setLanguage(AppLanguage.ENGLISH)
        assertEquals(AppLanguage.ENGLISH, progressManager.currentLanguage.value)
        assertEquals("Ancient Egypt", Localization.get("app_title", AppLanguage.ENGLISH))

        progressManager.setLanguage(AppLanguage.ARABIC)
        assertEquals(AppLanguage.ARABIC, progressManager.currentLanguage.value)
        assertEquals("حضارة مصر", Localization.get("app_title", AppLanguage.ARABIC))
    }

    @Test
    fun `test theme mode switching and persistence`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val progressManager = UserProgressManager(context)

        // Set to LIGHT
        progressManager.setThemeMode(com.example.model.ThemeMode.LIGHT)
        assertEquals(com.example.model.ThemeMode.LIGHT, progressManager.themeMode.value)

        // Toggle back to DARK
        progressManager.toggleThemeMode()
        assertEquals(com.example.model.ThemeMode.DARK, progressManager.themeMode.value)

        // Toggle back to LIGHT
        progressManager.toggleThemeMode()
        assertEquals(com.example.model.ThemeMode.LIGHT, progressManager.themeMode.value)
    }

    @Test
    fun `test historical eras progress calculation`() {
        val completedSections = setOf("pyramids_architecture")
        val quizScores = mapOf(
            "pyramids_architecture" to SectionQuizResult(
                sectionId = "pyramids_architecture",
                score = 5,
                totalQuestions = 5,
                percentage = 100,
                passed = true,
                completedAt = System.currentTimeMillis()
            )
        )

        val statuses = HistoricalErasData.calculateErasProgress(completedSections, quizScores)
        assertEquals(HistoricalErasData.eras.size, statuses.size)

        val oldKingdomStatus = statuses.find { it.era.id == "era_old_kingdom" }
        assertNotNull(oldKingdomStatus)
        assertTrue(oldKingdomStatus!!.isMastered)
        assertEquals(100, oldKingdomStatus.overallPercentage)
        assertEquals(1, oldKingdomStatus.completedSectionsCount)
        assertEquals(1, oldKingdomStatus.passedQuizzesCount)

        val newKingdomStatus = statuses.find { it.era.id == "era_new_kingdom" }
        assertNotNull(newKingdomStatus)
        assertFalse(newKingdomStatus!!.isStarted)
        assertEquals(0, newKingdomStatus.overallPercentage)
    }

    @Test
    fun `test historical eras entry points and landmarks data`() {
        // Verify all 6 eras exist
        assertEquals(6, HistoricalErasData.eras.size)

        HistoricalErasData.eras.forEach { era ->
            assertTrue("Era ${era.id} must have a name", era.nameAr.isNotBlank() && era.nameEn.isNotBlank())
            assertTrue("Era ${era.id} must have a period", era.periodAr.isNotBlank() && era.periodEn.isNotBlank())
            assertTrue("Era ${era.id} must have an icon emoji", era.iconEmoji.isNotBlank())
            assertTrue("Era ${era.id} must have at least one section id", era.sectionIds.isNotEmpty())
            assertTrue("Era ${era.id} must have landmarks defined", era.landmarksAr.isNotEmpty() && era.landmarksEn.isNotEmpty())
            assertNotNull("Era ${era.id} must have audio narration", era.audioNarration)
            assertTrue("Era audio duration must be positive", era.audioNarration!!.durationSeconds > 0)
        }

        // Verify localization keys for entry points component
        assertEquals("بوابات العصور التاريخية لمصر القديمة", Localization.get("eras_entry_points_title", AppLanguage.ARABIC))
        assertEquals("Gateways to Ancient Egyptian Eras", Localization.get("eras_entry_points_title", AppLanguage.ENGLISH))
        assertEquals("دخول واستكشاف العصر ➔", Localization.get("enter_era_portal", AppLanguage.ARABIC))
    }

    @Test
    fun `test welcome screen strings and onboarding progress`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val progressManager = UserProgressManager(context)

        // Verify welcome strings exist in Arabic and English
        assertEquals("حضارة مصر القديمة", Localization.get("welcome_screen_title", AppLanguage.ARABIC))
        assertEquals("Ancient Egyptian Civilization", Localization.get("welcome_screen_title", AppLanguage.ENGLISH))
        assertTrue(Localization.get("welcome_intro_p1", AppLanguage.ARABIC).contains("النيل"))
        assertTrue(Localization.get("welcome_intro_p1", AppLanguage.ENGLISH).contains("Nile"))

        // Verify onboarding completion toggling
        progressManager.setWelcomeCompleted(false)
        assertFalse(progressManager.hasSeenWelcome.value)

        progressManager.setWelcomeCompleted(true)
        assertTrue(progressManager.hasSeenWelcome.value)

        // Verify app logo drawable resource exists
        val logoResId = context.resources.getIdentifier("app_logo_egypt", "drawable", context.packageName)
        assertTrue("app_logo_egypt must exist in drawable resources", logoResId != 0)
    }
}
