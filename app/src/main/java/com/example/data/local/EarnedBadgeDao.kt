package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EarnedBadgeDao {

    @Query("SELECT * FROM earned_badges ORDER BY earnedAtTimestamp DESC")
    fun getAllEarnedBadges(): Flow<List<EarnedBadgeEntity>>

    @Query("SELECT * FROM earned_badges WHERE badgeId = :badgeId LIMIT 1")
    fun getEarnedBadgeById(badgeId: String): Flow<EarnedBadgeEntity?>

    @Query("SELECT * FROM earned_badges WHERE badgeId = :badgeId LIMIT 1")
    suspend fun findBadgeDirect(badgeId: String): EarnedBadgeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEarnedBadge(badge: EarnedBadgeEntity)

    @Query("SELECT COUNT(*) FROM earned_badges")
    fun getEarnedBadgesCount(): Flow<Int>

    @Query("DELETE FROM earned_badges WHERE badgeId = :badgeId")
    suspend fun deleteBadge(badgeId: String)
}
