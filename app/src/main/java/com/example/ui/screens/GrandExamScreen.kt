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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SectionQuizResult
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight
import com.example.ui.theme.PureBlack

@Composable
fun GrandExamScreen(
    lastResult: SectionQuizResult?,
    onStartExam: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = "امتحان التحدي الأكبر للحضارة",
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("grand_exam_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Intro Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = NileLapisDark),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        Brush.linearGradient(listOf(PharaohGoldLight, PharaohGold, PharaohGoldDark))
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(PharaohGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🦅", fontSize = 40.sp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "امتحان التحدي الأكبر",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PharaohGold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "الاختبار الشامل لفرسان وعلماء الحضارة المصرية القديمة",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (lastResult != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PharaohGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    PharaohGold
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = PharaohGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "أعلى درجة أحرزتها: ${lastResult.percentage}% (${lastResult.score} من ${lastResult.totalQuestions})",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Instructions Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "قواعد التحدي الملكي:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        RuleItem(
                            icon = "🔟",
                            title = "10 أسئلة متنوعة",
                            description = "مختارة عشوائياً من كافة أقسام الحضارة (الأهرامات، الفراعنة، الآلهة، العلوم، الطب، والنيل)."
                        )

                        RuleItem(
                            icon = "🎯",
                            title = "80% للنجاح ونيل اللقب",
                            description = "تحتاج للإجابة الصحيحة على 8 أسئلة على الأقل لنيل وسام 'حكيم مصر الأكبر'."
                        )

                        RuleItem(
                            icon = "💡",
                            title = "تفسيرات تاريخية موثقة",
                            description = "ستتعرف بعد كل سؤال على الشرح الأثري الموثق والمعلومة الفرعونية المرافقة."
                        )
                    }
                }
            }

            // Launch Button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onStartExam,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("launch_grand_exam_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharaohGold,
                        contentColor = PureBlack
                    )
                ) {
                    Icon(
                        Icons.Default.Quiz,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = PureBlack
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lastResult != null) "خوض الامتحان مرة أخرى ⚡" else "ابدأ التحدي الأكبر الآن ⚡",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun RuleItem(icon: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(icon, fontSize = 18.sp)
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
