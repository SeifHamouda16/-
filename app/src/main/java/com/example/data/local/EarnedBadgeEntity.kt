package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earned_badges")
data class EarnedBadgeEntity(
    @PrimaryKey
    val badgeId: String,
    val title: String,
    val description: String,
    val glyphEmoji: String,
    val sectionId: String,
    val scorePercentage: Int,
    val quizScore: Int,
    val totalQuestions: Int,
    val earnedAtTimestamp: Long = System.currentTimeMillis()
)
