package com.example.data.local

import com.example.model.PharaonicBadge
import kotlinx.coroutines.flow.Flow

class EarnedBadgeRepository(private val dao: EarnedBadgeDao) {

    val allEarnedBadges: Flow<List<EarnedBadgeEntity>> = dao.getAllEarnedBadges()
    val earnedCount: Flow<Int> = dao.getEarnedBadgesCount()

    suspend fun saveEarnedBadge(
        badge: PharaonicBadge,
        score: Int,
        totalQuestions: Int,
        percentage: Int
    ): EarnedBadgeEntity {
        val entity = EarnedBadgeEntity(
            badgeId = badge.id,
            title = badge.title,
            description = badge.description,
            glyphEmoji = badge.glyphEmoji,
            sectionId = badge.requiredSectionId,
            scorePercentage = percentage,
            quizScore = score,
            totalQuestions = totalQuestions,
            earnedAtTimestamp = System.currentTimeMillis()
        )
        dao.insertEarnedBadge(entity)
        return entity
    }

    suspend fun hasBadge(badgeId: String): Boolean {
        return dao.findBadgeDirect(badgeId) != null
    }

    fun getBadgeById(badgeId: String): Flow<EarnedBadgeEntity?> {
        return dao.getEarnedBadgeById(badgeId)
    }
}
