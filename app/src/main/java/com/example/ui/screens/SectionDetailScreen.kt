package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import com.example.data.HistoricalErasData
import com.example.model.AppLanguage
import com.example.model.AudioPlaybackState
import com.example.model.CurrentAudioState
import com.example.model.HistoricalEra
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CivilizationSection
import com.example.model.PharaohSecret
import com.example.model.SectionQuizResult
import com.example.model.SubTopic
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.MalachiteGreen
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

@Composable
fun SectionDetailScreen(
    section: CivilizationSection,
    isCompleted: Boolean,
    quizResult: SectionQuizResult?,
    isBookmarked: Boolean,
    onNavigateBack: () -> Unit,
    onMarkCompleted: () -> Unit,
    onToggleBookmark: () -> Unit,
    onStartQuiz: () -> Unit,
    onToggleTheme: () -> Unit = {},
    isDarkTheme: Boolean = true,
    audioState: CurrentAudioState = CurrentAudioState(),
    currentLanguage: AppLanguage = AppLanguage.ARABIC,
    onPlayEraAudio: (HistoricalEra) -> Unit = {},
    onToggleAudioPlayback: () -> Unit = {},
    onOpenAudioDialog: (HistoricalEra) -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = section.title,
                onNavigateBack = onNavigateBack,
                onToggleTheme = onToggleTheme,
                isDarkTheme = isDarkTheme,
                actions = {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.testTag("section_bookmark_button")
                    ) {
                        Icon(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "حفظ القسم",
                            tint = if (isBookmarked) PharaohGold else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Prominent Bottom Quiz Bar - core user requirement
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("section_bottom_quiz_bar"),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (quizResult != null) "درجتك السابقة: ${quizResult.percentage}%" else "أنهيت قراءة هذا الجزء؟",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (quizResult?.passed == true) MalachiteGreen else PharaohGold
                        )
                        Text(
                            text = "اختبر معلوماتك في ${section.quiz.size} أسئلة",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = onStartQuiz,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PharaohGold,
                            contentColor = NileLapisDark
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("start_section_quiz_cta")
                    ) {
                        Icon(
                            Icons.Default.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (quizResult != null) "إعادة الامتحان" else "ابدأ الامتحان الآن",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("section_detail_content"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Hero Image Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Image(
                        painter = painterResource(id = section.drawableResId),
                        contentDescription = section.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = section.iconEmoji, fontSize = 28.sp)
                            Text(
                                text = section.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = section.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = PharaohGoldLight,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Quick Info Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = PharaohGold
                        )
                        Text(
                            text = "${section.readTimeMinutes} دقائق قراءة",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.HelpOutline,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = PharaohGold
                        )
                        Text(
                            text = "${section.quiz.size} أسئلة اختبار",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Mark as read button / check
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCompleted) MalachiteGreen.copy(alpha = 0.15f) else PharaohGold.copy(alpha = 0.15f),
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (isCompleted) MalachiteGreen else PharaohGold
                            )
                            Text(
                                text = if (isCompleted) "مكتمل" else "قيد التعلم",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) MalachiteGreen else PharaohGold
                            )
                        }
                    }
                }
            }

            // Era & Overview Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "العصر التاريخي: ${section.era}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = section.summary,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Era Audio Narration Card
            item {
                val era = remember(section.id) {
                    HistoricalErasData.eras.find { section.id in it.sectionIds }
                }
                if (era?.audioNarration != null) {
                    val narration = era.audioNarration
                    val isPlayingThisEra = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING
                    val isPausedThisEra = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PAUSED
                    val audioTitle = if (currentLanguage == AppLanguage.ARABIC) narration.titleAr else narration.titleEn

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("section_era_audio_card")
                            .clickable {
                                if (isPlayingThisEra || isPausedThisEra) {
                                    onOpenAudioDialog(era)
                                } else {
                                    onPlayEraAudio(era)
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isPlayingThisEra) PharaohGold.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.2.dp,
                            if (isPlayingThisEra) MalachiteGreen else PharaohGold
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = PharaohGold.copy(alpha = 0.2f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold),
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Headphones,
                                                contentDescription = null,
                                                tint = PharaohGold,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) "مقطع صوتي توضيحي لهذا العصر" else "Explanatory Audio Clip for this Era",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = PharaohGold
                                        )
                                        Text(
                                            text = audioTitle,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        if (isPlayingThisEra || isPausedThisEra) {
                                            onToggleAudioPlayback()
                                        } else {
                                            onPlayEraAudio(era)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isPlayingThisEra) MalachiteGreen else PharaohGold,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("section_era_audio_play_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingThisEra) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlayingThisEra) "إيقاف مؤقت" else "تشغيل",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isPlayingThisEra) (if (currentLanguage == AppLanguage.ARABIC) "إيقاف" else "Pause")
                                        else (if (currentLanguage == AppLanguage.ARABIC) "استمع (${narration.durationSeconds}ث)" else "Listen (${narration.durationSeconds}s)"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            val scriptPreview = if (currentLanguage == AppLanguage.ARABIC) narration.narratorScriptAr else narration.narratorScriptEn
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = scriptPreview,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Subtopics Header
            item {
                Text(
                    text = "محاور وأسرار هذا الجزء",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PharaohGold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // SubTopics Cards
            items(section.subTopics, key = { it.id }) { subTopic ->
                SubTopicCard(subTopic = subTopic)
            }

            // Pharaoh Secrets Section
            if (section.secrets.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "أسرار فرعونية غامضة ومدهشة ⚡",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                items(section.secrets) { secret ->
                    PharaohSecretCard(secret = secret)
                }
            }

            // End of reading CTA card
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("ready_for_quiz_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NileLapisDark),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(PharaohGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📜", fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "أحسنت! أنهيت قراءة المعرفة",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "حان وقت قياس استيعابك وكسب وسام هذا القسم في امتحان تفاعلي فوري من ${section.quiz.size} أسئلة!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onMarkCompleted()
                                onStartQuiz()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("take_quiz_button_main"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PharaohGold,
                                contentColor = NileLapisDark
                            )
                        ) {
                            Icon(
                                Icons.Default.Quiz,
                                contentDescription = null,
                                tint = NileLapisDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "خُض الامتحان الآن ⚡",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubTopicCard(subTopic: SubTopic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = subTopic.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subTopic.content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (subTopic.keyPoints.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp)
                ) {
                    Text(
                        text = "نقاط جوهرية:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                    subTopic.keyPoints.forEach { point ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("✦", color = PharaohGold, fontSize = 12.sp)
                            Text(
                                text = point,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            if (subTopic.quoteOrInscription != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PharaohGold.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "« ${subTopic.quoteOrInscription} »",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = PharaohGoldDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PharaohSecretCard(secret: PharaohSecret) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = PharaohGold.copy(alpha = 0.08f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = secret.icon, fontSize = 24.sp)
            Column {
                Text(
                    text = secret.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PharaohGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = secret.secretText,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
