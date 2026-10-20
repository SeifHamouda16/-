package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.model.QuizQuestion
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.MalachiteGreen
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight
import com.example.ui.theme.TerracottaRed

@Composable
fun QuizScreen(
    section: CivilizationSection?,
    isGrandExam: Boolean,
    questions: List<QuizQuestion>,
    currentIndex: Int,
    selectedOption: Int?,
    isConfirmed: Boolean,
    onSelectOption: (Int) -> Unit,
    onConfirmAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onExitQuiz()
    }

    val currentQuestion = questions.getOrNull(currentIndex)
    val totalCount = questions.size
    val title = if (isGrandExam) "امتحان التحدي الأكبر (شامل)" else "امتحان: ${section?.title ?: ""}"

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = title,
                onNavigateBack = onExitQuiz
            )
        }
    ) { innerPadding ->
        if (currentQuestion == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("لا توجد أسئلة حالياً")
            }
            return@Scaffold
        }

        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .testTag("quiz_screen_container")
        ) {
            // Header Progress Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "السؤال ${currentIndex + 1} من $totalCount",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PharaohGold
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PharaohGold.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${((currentIndex + 1) * 100) / totalCount}% مكتمل",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { ((currentIndex + 1).toFloat() / totalCount.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .testTag("quiz_progress_indicator"),
                color = PharaohGold,
                trackColor = PharaohGold.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("question_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(PharaohGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Quiz,
                                contentDescription = null,
                                tint = NileLapisDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "سؤال الفراعنة رقم ${currentIndex + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGoldDark
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 30.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List
            Text(
                text = "اختر الإجابة الصحيحة:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            currentQuestion.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isCorrect = index == currentQuestion.correctIndex

                val (backgroundColor, borderColor, textColor) = when {
                    !isConfirmed -> {
                        if (isSelected) {
                            Triple(
                                PharaohGold.copy(alpha = 0.2f),
                                PharaohGold,
                                MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Triple(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    isCorrect -> {
                        Triple(
                            MalachiteGreen.copy(alpha = 0.2f),
                            MalachiteGreen,
                            MaterialTheme.colorScheme.onSurface
                        )
                    }
                    isSelected -> {
                        Triple(
                            TerracottaRed.copy(alpha = 0.2f),
                            TerracottaRed,
                            MaterialTheme.colorScheme.onSurface
                        )
                    }
                    else -> {
                        Triple(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !isConfirmed) { onSelectOption(index) }
                        .testTag("quiz_option_$index"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Option letter badge
                        val letterBadge = when (index) {
                            0 -> "أ"
                            1 -> "ب"
                            2 -> "ج"
                            3 -> "د"
                            else -> "${index + 1}"
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isConfirmed && isCorrect -> MalachiteGreen
                                        isConfirmed && isSelected -> TerracottaRed
                                        isSelected -> PharaohGold
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isConfirmed && isCorrect) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            } else if (isConfirmed && isSelected) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = letterBadge,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (isSelected) NileLapisDark else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected || (isConfirmed && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                            color = textColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Confirmed Explanation Card
            AnimatedVisibility(
                visible = isConfirmed,
                enter = fadeIn() + expandVertically()
            ) {
                val isSelectedCorrect = selectedOption == currentQuestion.correctIndex

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelectedCorrect) MalachiteGreen.copy(alpha = 0.12f) else TerracottaRed.copy(alpha = 0.12f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelectedCorrect) MalachiteGreen else TerracottaRed
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (isSelectedCorrect) "🎉 إجابة صحيحة وممتازة!" else "🏺 إجابة خاطئة!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelectedCorrect) MalachiteGreen else TerracottaRed
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "التفسير التاريخي:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = currentQuestion.explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        PharaohGold.copy(alpha = 0.15f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = PharaohGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = currentQuestion.historicalFunFact,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Button
            if (!isConfirmed) {
                Button(
                    onClick = onConfirmAnswer,
                    enabled = selectedOption != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_quiz_answer_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharaohGold,
                        contentColor = NileLapisDark,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "تأكيد الإجابة",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                val isLast = currentIndex == totalCount - 1
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("next_quiz_question_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharaohGold,
                        contentColor = NileLapisDark
                    )
                ) {
                    Text(
                        text = if (isLast) "عرض النتيجة النهائية 🏆" else "السؤال التالي ➔",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
