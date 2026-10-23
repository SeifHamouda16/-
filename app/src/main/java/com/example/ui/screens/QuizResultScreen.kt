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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CivilizationSection
import com.example.model.PharaonicBadge
import com.example.model.SectionQuizResult
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TerracottaRed
import com.example.viewmodel.UserQuestionAnswer

@Composable
fun QuizResultScreen(
    section: CivilizationSection?,
    isGrandExam: Boolean,
    result: SectionQuizResult,
    userAnswers: List<UserQuestionAnswer>,
    unlockedBadge: PharaonicBadge?,
    onRetakeQuiz: () -> Unit,
    onBackToHome: () -> Unit,
    onBackToSection: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackToHome()
    }

    val passed = result.passed
    val title = if (isGrandExam) "نتيجة امتحان التحدي الأكبر" else "نتيجة امتحان: ${section?.title ?: ""}"

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = title,
                onNavigateBack = onBackToHome
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("quiz_result_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Celebration Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("score_celebration_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (passed) NileLapisDark else MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (passed) PharaohGold else TerracottaRed
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Badge / Trophy Icon
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(if (passed) PharaohGold else TerracottaRed.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (passed) "👑" else "🏺",
                                fontSize = 36.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (passed) "مبارك! اجتزت الامتحان بنجاح باهر" else "محاولة جيدة! راجع التاريخ وحاول ثانية",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (passed) PharaohGold else TerracottaRed
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "حصلت على ${result.score} إجابات صحيحة من أصل ${result.totalQuestions}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (passed) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Large Score Percentage Pill
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = if (passed) PharaohGold else TerracottaRed,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "الدرجة النهائية: ${result.percentage}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (passed) NileLapisDark else Color.White,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (passed) "+${result.score * 20} نقطة مجد فرعوني أضيفت لرصيدك!" else "الحد الأدنى للنجاح هو 70% لنيل الأوسمة الملكية",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (passed) PharaohGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Unlocked Badge Card (if any)
            if (unlockedBadge != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("unlocked_badge_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PharaohGold.copy(alpha = 0.15f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(PharaohGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(unlockedBadge.glyphEmoji, fontSize = 28.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "وسام ملكي جديد تم تحريره! 🎖️",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGoldDark
                                )
                                Text(
                                    text = unlockedBadge.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = unlockedBadge.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetakeQuiz,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("retake_quiz_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = PharaohGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إعادة الاختبار", color = PharaohGold, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (section != null) onBackToSection() else onBackToHome()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("finish_quiz_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PharaohGold,
                            contentColor = NileLapisDark
                        )
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = NileLapisDark, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (section != null) "متابعة التعلم" else "الرئيسية",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Question Review Header
            item {
                Text(
                    text = "مراجعة إجاباتك بالتفصيل:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Question Review Items
            itemsIndexed(userAnswers) { index, answer ->
                val q = answer.question
                val isCorrect = answer.isCorrect
                val selectedText = q.options.getOrNull(answer.selectedIndex) ?: ""
                val correctText = q.options.getOrNull(q.correctIndex) ?: ""

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCorrect) PharaohGold.copy(alpha = 0.5f) else TerracottaRed.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isCorrect) PharaohGold else TerracottaRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (isCorrect) PureBlack else Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                text = "السؤال ${index + 1}: ${q.question}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // User's answer
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "إجابتك:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) PharaohGold else TerracottaRed
                            )
                            Text(
                                text = selectedText,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCorrect) PharaohGold else TerracottaRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (!isCorrect) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "الإجابة الصحيحة:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text(
                                    text = correctText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PharaohGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "💡 ${q.explanation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
