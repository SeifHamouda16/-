package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CivilizationData
import com.example.data.HistoricalErasData
import com.example.data.local.EarnedBadgeEntity
import com.example.model.PharaonicBadge
import com.example.model.SectionQuizResult
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AchievementsScreen(
    userTitle: Pair<String, String>,
    totalPoints: Int,
    unlockedBadges: Set<String>,
    roomEarnedBadges: List<EarnedBadgeEntity>,
    quizScores: Map<String, SectionQuizResult>,
    onNavigateBack: () -> Unit,
    onToggleTheme: () -> Unit = {},
    isDarkTheme: Boolean = true,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val roomBadgeMap = roomEarnedBadges.associateBy { it.badgeId }

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = "بهو الأوسمة والإنجازات",
                onNavigateBack = onNavigateBack,
                onToggleTheme = onToggleTheme,
                isDarkTheme = isDarkTheme
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("achievements_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rank Header Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NileLapisDark),
                    border = androidx.compose.foundation.BorderStroke(2.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(PharaohGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👑", fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = userTitle.first,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = PharaohGold
                        )

                        Text(
                            text = userTitle.second,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$totalPoints",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text(
                                    text = "إجمالي النقاط",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${roomEarnedBadges.size} / ${CivilizationData.badges.size}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text(
                                    text = "الأوسمة المحررة (Room)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${quizScores.values.count { it.passed }}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text(
                                    text = "امتحانات ناجحة",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            // Database persistence banner
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Storage,
                            contentDescription = null,
                            tint = PharaohGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "قاعدة بيانات خلود الأوسمة (Room Database)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                            Text(
                                text = "تم حفظ وتوثيق ${roomEarnedBadges.size} وساماً محلياً على جهازك بشكل دائم",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Badges Section Header
            item {
                Text(
                    text = "سجل الأوسمة الملكية (${roomEarnedBadges.size} من ${CivilizationData.badges.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Badges List
            items(CivilizationData.badges) { badge ->
                val roomEntity = roomBadgeMap[badge.id]
                val isUnlocked = roomEntity != null || unlockedBadges.contains(badge.id)
                BadgeItemCard(badge = badge, isUnlocked = isUnlocked, roomEntity = roomEntity)
            }

            // Eras Mastery Progress in Hall of Fame
            item {
                val erasProgress = remember(quizScores) {
                    HistoricalErasData.calculateErasProgress(
                        quizScores.keys.toSet(),
                        quizScores
                    )
                }
                val mastered = erasProgress.count { it.isMastered }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("achievements_eras_mastery_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🏛️", fontSize = 22.sp)
                                Text(
                                    text = "إتقان الحقب التاريخية",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PharaohGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                            ) {
                                Text(
                                    text = "$mastered / ${erasProgress.size} حقب متقنة 👑",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        erasProgress.forEach { status ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(status.era.iconEmoji, fontSize = 16.sp)
                                    Column {
                                        Text(
                                            text = status.era.nameAr,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = status.era.periodAr,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PharaohGoldDark,
                                            fontSize = 9.sp
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = { (status.overallPercentage / 100f).coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .width(70.dp)
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = PharaohGold,
                                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                                    Text(
                                        text = "${status.overallPercentage}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Royal Certificate Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        Brush.linearGradient(listOf(PharaohGoldLight, PharaohGold, PharaohGoldDark))
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📜 شهادة خبير التاريخ المصري القديم",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "تُمنح هذه الشهادة الفخرية لمن أتم استكشاف معالم وأسرار أقدم حضارات الأرض بنجاح واجتاز اختباراتها المعرفية.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PharaohGold.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                        ) {
                            Text(
                                text = "ختم حورس وتحوت الملكي ☥",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGoldDark,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeItemCard(badge: PharaonicBadge, isUnlocked: Boolean, roomEntity: EarnedBadgeEntity? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) PharaohGold.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnlocked) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) PharaohGold else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(text = badge.glyphEmoji, fontSize = 28.sp)
                } else {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "مغلق",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = badge.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) PharaohGold else MaterialTheme.colorScheme.onSurface
                    )

                    if (isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PharaohGold.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, PharaohGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = PharaohGold, modifier = Modifier.size(12.dp))
                                Text("مُكتسب بالذهب", fontSize = 10.sp, color = PharaohGold, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (roomEntity != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    val dateStr = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        .format(Date(roomEntity.earnedAtTimestamp))
                    Text(
                        text = "🏛️ حُفظ في Room: بنسبة ${roomEntity.scorePercentage}% في $dateStr",
                        style = MaterialTheme.typography.labelSmall,
                        color = PharaohGoldDark,
                        fontWeight = FontWeight.SemiBold
                    )
                } else if (!isUnlocked) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🔒 يتطلب إحراز ${badge.minimumScorePercent}% في امتحان هذا القسم",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
