package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.local.EarnedBadgeEntity
import com.example.model.CivilizationSection
import com.example.model.PharaonicBadge
import com.example.model.SectionQuizResult
import com.example.model.UserProfile
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class CloudSyncResult(
    val success: Boolean,
    val message: String,
    val downloadedProfile: UserProfile? = null,
    val completedSections: Set<String> = emptySet(),
    val quizScores: Map<String, SectionQuizResult> = emptyMap(),
    val unlockedBadges: Set<String> = emptySet()
)

data class CloudBroadcastUpdate(
    val title: String = "",
    val message: String = "",
    val version: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class FirebaseProgressRepository(private val context: Context) {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    }

    private val auth: FirebaseAuth get() = Firebase.auth

    val currentUser get() = auth.currentUser

    fun getUserId(): String? = auth.currentUser?.uid

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("يجب تسجيل الدخول بواسطة Google للوصول إلى قاعدة بيانات Firebase.")
    }

    /**
     * Upload full local progress to Firestore immediately.
     */
    suspend fun uploadAllProgressToCloud(
        profile: UserProfile,
        completedSections: Set<String>,
        quizScores: Map<String, SectionQuizResult>,
        badges: List<EarnedBadgeEntity>,
        totalPoints: Int,
        pharaohTitle: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val uid = requireUserId()
            val userDocRef = db.collection("users").document(uid)

            // 1. Upload UserProfile
            val profilePayload = hashMapOf<String, Any>(
                "userId" to uid,
                "name" to (profile.name.ifBlank { "باحث الفراعنة" }),
                "email" to (profile.email.ifBlank { auth.currentUser?.email ?: "explorer@egypt.ancient" }),
                "avatarEmoji" to profile.avatarEmoji,
                "avatarId" to profile.avatarId,
                "totalPoints" to totalPoints,
                "pharaohTitle" to pharaohTitle,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            userDocRef.set(profilePayload, SetOptions.merge()).await()

            // 2. Upload Completed Sections
            val completedCol = userDocRef.collection("completedSections")
            for (sectionId in completedSections) {
                val data = hashMapOf<String, Any>(
                    "sectionId" to sectionId,
                    "userId" to uid,
                    "completedAt" to FieldValue.serverTimestamp()
                )
                completedCol.document(sectionId).set(data, SetOptions.merge()).await()
            }

            // 3. Upload Quiz Results
            val quizCol = userDocRef.collection("quizResults")
            for ((sectionId, result) in quizScores) {
                val data = hashMapOf<String, Any>(
                    "sectionId" to sectionId,
                    "userId" to uid,
                    "score" to result.score,
                    "totalQuestions" to result.totalQuestions,
                    "percentage" to result.percentage,
                    "passed" to result.passed,
                    "completedAt" to FieldValue.serverTimestamp()
                )
                quizCol.document(sectionId).set(data, SetOptions.merge()).await()
            }

            // 4. Upload Earned Badges
            val badgeCol = userDocRef.collection("earnedBadges")
            for (badge in badges) {
                val data = hashMapOf<String, Any>(
                    "badgeId" to badge.badgeId,
                    "userId" to uid,
                    "title" to badge.title,
                    "description" to badge.description,
                    "glyphEmoji" to badge.glyphEmoji,
                    "earnedAt" to FieldValue.serverTimestamp()
                )
                badgeCol.document(badge.badgeId).set(data, SetOptions.merge()).await()
            }

            // 5. Broadcast device update timestamp so other devices pick it up
            val broadcastDoc = db.collection("app_updates").document("latest")
            val broadcastData = hashMapOf<String, Any>(
                "title" to "تحديث بيانات الأجهزة",
                "message" to "تم حفظ تقدم فرعوني جديد ومزامنته سحابياً بنجاح.",
                "updatedByUid" to uid,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            broadcastDoc.set(broadcastData, SetOptions.merge()).await()

            Log.i("FirebaseRepo", "تم رفع ومزامنة كافة البيانات على سحابة Firestore بنجاح للمستخدم: $uid")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "فشل الرفع إلى Firebase: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Download and pull progress from Firestore to sync this device.
     */
    suspend fun downloadProgressFromCloud(): CloudSyncResult = withContext(Dispatchers.IO) {
        val uid = getUserId() ?: return@withContext CloudSyncResult(
            success = false,
            message = "يرجى تسجيل الدخول بحساب Google أولاً لمزامنة البيانات."
        )

        try {
            val userDocRef = db.collection("users").document(uid)
            val profileSnap = userDocRef.get().await()

            if (!profileSnap.exists()) {
                return@withContext CloudSyncResult(
                    success = true,
                    message = "تم الاتصال بالسحابة: لا توجد بيانات سابقة لهذا الحساب، يمكنك رفع بياناتك الآن."
                )
            }

            val name = profileSnap.getString("name") ?: "باحث الفراعنة"
            val email = profileSnap.getString("email") ?: (auth.currentUser?.email ?: "")
            val avatarEmoji = profileSnap.getString("avatarEmoji") ?: "👑"
            val avatarId = profileSnap.getString("avatarId") ?: "pharaoh"

            val pulledProfile = UserProfile(
                isLoggedIn = true,
                name = name,
                email = email,
                avatarEmoji = avatarEmoji,
                avatarId = avatarId,
                lastSyncTimestamp = System.currentTimeMillis()
            )

            // Download completed sections
            val completedSnap = userDocRef.collection("completedSections").get().await()
            val completedSet = completedSnap.documents.mapNotNull { it.getString("sectionId") }.toSet()

            // Download quiz scores
            val quizSnap = userDocRef.collection("quizResults").get().await()
            val quizMap = mutableMapOf<String, SectionQuizResult>()
            for (doc in quizSnap.documents) {
                val secId = doc.getString("sectionId") ?: doc.id
                val score = doc.getLong("score")?.toInt() ?: 0
                val total = doc.getLong("totalQuestions")?.toInt() ?: 5
                val percent = doc.getLong("percentage")?.toInt() ?: 0
                val passed = doc.getBoolean("passed") ?: false
                quizMap[secId] = SectionQuizResult(
                    sectionId = secId,
                    score = score,
                    totalQuestions = total,
                    percentage = percent,
                    passed = passed,
                    completedAt = System.currentTimeMillis()
                )
            }

            // Download earned badges
            val badgeSnap = userDocRef.collection("earnedBadges").get().await()
            val badgeSet = badgeSnap.documents.mapNotNull { it.getString("badgeId") }.toSet()

            CloudSyncResult(
                success = true,
                message = "تمت المزامنة وسحب بياناتك بنجاح من السحابة! ☥",
                downloadedProfile = pulledProfile,
                completedSections = completedSet,
                quizScores = quizMap,
                unlockedBadges = badgeSet
            )
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "خطأ أثناء سحب البيانات السحابية: ${e.message}", e)
            CloudSyncResult(
                success = false,
                message = "تعذر الاتصال بالسحابة: ${e.localizedMessage ?: "تحقق من اتصال الإنترنت"}"
            )
        }
    }

    /**
     * Real-time flow of broadcast updates across devices.
     */
    fun observeCloudBroadcast(): Flow<CloudBroadcastUpdate?> = flow {
        val docRef = db.collection("app_updates").document("latest")
        docRef.snapshots()
            .map { snap ->
                if (snap.exists()) {
                    CloudBroadcastUpdate(
                        title = snap.getString("title") ?: "تحديث سحابي",
                        message = snap.getString("message") ?: "بيانات محدثة متاحة",
                        version = snap.getString("version") ?: "1.0",
                        timestamp = (snap.getTimestamp("updatedAt") ?: Timestamp.now()).toDate().time
                    )
                } else {
                    null
                }
            }
            .catch { e ->
                Log.w("FirebaseRepo", "فشل الاستماع للتحديثات السحابية: ${e.message}")
                emit(null)
            }
            .collect { emit(it) }
    }
}
