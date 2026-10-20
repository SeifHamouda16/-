package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.CivilizationData
import com.example.data.HistoricalErasData
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.AudioPlaybackState
import com.example.model.CivilizationSection
import com.example.model.CurrentAudioState
import com.example.model.EraCompletionStatus
import com.example.model.HistoricalEra
import com.example.model.SectionQuizResult
import com.example.ui.theme.MalachiteGreen
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

@Composable
fun EraProgressTracker(
    completedSections: Set<String>,
    quizScores: Map<String, SectionQuizResult>,
    currentLanguage: AppLanguage,
    onSectionClick: (CivilizationSection) -> Unit,
    onStartQuizClick: (CivilizationSection) -> Unit,
    onPlayAudio: (HistoricalEra) -> Unit = {},
    onOpenEraDetail: (HistoricalEra) -> Unit = {},
    audioState: CurrentAudioState = CurrentAudioState(),
    modifier: Modifier = Modifier
) {
    val eraStatuses = remember(completedSections, quizScores) {
        HistoricalErasData.calculateErasProgress(completedSections, quizScores)
    }

    var isCarouselView by remember { mutableStateOf(true) }
    var selectedEraForDialog by remember { mutableStateOf<EraCompletionStatus?>(null) }

    val totalEras = eraStatuses.size
    val masteredEras = eraStatuses.count { it.isMastered }
    val inProgressEras = eraStatuses.count { it.isStarted && !it.isMastered }
    val notStartedEras = eraStatuses.count { !it.isStarted }
    val averageOverallProgress = if (totalEras > 0) eraStatuses.sumOf { it.overallPercentage } / totalEras else 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("era_progress_tracker")
            .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Title, Icon, and View Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PharaohGold.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = Localization.get("eras_tracker_title", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Localization.get("eras_tracker_subtitle", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Switch between Carousel and Vertical List
                IconButton(
                    onClick = { isCarouselView = !isCarouselView },
                    modifier = Modifier.testTag("era_toggle_view_btn")
                ) {
                    Icon(
                        imageVector = if (isCarouselView) Icons.Default.ViewAgenda else Icons.Default.ViewCarousel,
                        contentDescription = if (isCarouselView) Localization.get("view_all_eras", currentLanguage) else Localization.get("collapse_eras", currentLanguage),
                        tint = PharaohGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Overall Mastery Metric Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "$averageOverallProgress%",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = PharaohGold
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "إجمالي استكشاف الحقب" else "Total Eras Explored",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { (averageOverallProgress / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PharaohGold,
                        trackColor = PharaohGold.copy(alpha = 0.2f)
                    )
                }

                // Status Pills Summary
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatusCounterPill(
                        count = masteredEras,
                        label = Localization.get("eras_mastered_count", currentLanguage),
                        color = MalachiteGreen,
                        icon = "👑"
                    )
                    StatusCounterPill(
                        count = inProgressEras,
                        label = Localization.get("eras_in_progress_count", currentLanguage),
                        color = PharaohGold,
                        icon = "⚡"
                    )
                    StatusCounterPill(
                        count = notStartedEras,
                        label = Localization.get("eras_not_started_count", currentLanguage),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        icon = "🔒"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Carousel or Expanded List View
            if (isCarouselView) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(eraStatuses, key = { it.era.id }) { status ->
                        EraCardCompact(
                            status = status,
                            currentLanguage = currentLanguage,
                            audioState = audioState,
                            onClick = { selectedEraForDialog = status },
                            onExplore = {
                                val sec = CivilizationData.sections.find { it.id in status.era.sectionIds }
                                if (sec != null) onSectionClick(sec)
                            },
                            onPlayAudio = { onPlayAudio(status.era) }
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    eraStatuses.forEach { status ->
                        EraCardDetailed(
                            status = status,
                            currentLanguage = currentLanguage,
                            audioState = audioState,
                            onClick = { selectedEraForDialog = status },
                            onExplore = {
                                val sec = CivilizationData.sections.find { it.id in status.era.sectionIds }
                                if (sec != null) onSectionClick(sec)
                            },
                            onStartQuiz = {
                                val sec = CivilizationData.sections.find { it.id in status.era.sectionIds }
                                if (sec != null) onStartQuizClick(sec)
                            },
                            onPlayAudio = { onPlayAudio(status.era) }
                        )
                    }
                }
            }
        }
    }

    // Detailed Era Modal Dialog when tapped
    if (selectedEraForDialog != null) {
        val status = selectedEraForDialog!!
        EraDetailDialog(
            status = status,
            currentLanguage = currentLanguage,
            audioState = audioState,
            onDismiss = { selectedEraForDialog = null },
            onExploreSection = { section ->
                selectedEraForDialog = null
                onSectionClick(section)
            },
            onStartQuizSection = { section ->
                selectedEraForDialog = null
                onStartQuizClick(section)
            },
            onPlayAudio = { era ->
                onPlayAudio(era)
            },
            onOpenEraDetail = { era ->
                selectedEraForDialog = null
                onOpenEraDetail(era)
            }
        )
    }
}

@Composable
private fun StatusCounterPill(
    count: Int,
    label: String,
    color: Color,
    icon: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$icon $count", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 9.sp, color = color, maxLines = 1)
        }
    }
}

@Composable
private fun EraCardCompact(
    status: EraCompletionStatus,
    currentLanguage: AppLanguage,
    audioState: CurrentAudioState,
    onClick: () -> Unit,
    onExplore: () -> Unit,
    onPlayAudio: () -> Unit
) {
    val era = status.era
    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val isAudioPlayingThisEra = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING

    Card(
        modifier = Modifier
            .width(230.dp)
            .testTag("era_card_${era.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (status.isMastered) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (status.isMastered) 1.5.dp else 1.dp,
            if (status.isMastered) MalachiteGreen else if (status.isStarted) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (status.isMastered) MalachiteGreen.copy(alpha = 0.2f) else PharaohGold.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (status.isMastered) MalachiteGreen else PharaohGold),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(era.iconEmoji, fontSize = 18.sp)
                    }
                }

                // Status Chip
                EraStatusBadge(status = status, currentLanguage = currentLanguage)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = eraTitle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = eraPeriod,
                style = MaterialTheme.typography.bodySmall,
                color = PharaohGoldDark,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Indicators Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = if (status.completedSectionsCount > 0) MalachiteGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${status.completedSectionsCount}/${status.totalSections}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = if (status.passedQuizzesCount > 0) PharaohGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (status.quizPercentage > 0) "${status.quizPercentage}%" else "-",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (status.quizPercentage >= 70) MalachiteGreen else PharaohGold
                    )
                }

                Text(
                    text = "${status.overallPercentage}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (status.isMastered) MalachiteGreen else PharaohGold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (status.overallPercentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (status.isMastered) MalachiteGreen else PharaohGold,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Explore & Audio Guide
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onExplore,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("era_explore_btn_${era.id}"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                ) {
                    Text(
                        text = Localization.get("era_explore_btn", currentLanguage),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                }

                Button(
                    onClick = onPlayAudio,
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .testTag("era_audio_btn_${era.id}"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAudioPlayingThisEra) MalachiteGreen else PharaohGold,
                        contentColor = Color.Black
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(if (isAudioPlayingThisEra) "⏸️" else "🎙️", fontSize = 11.sp)
                        Text(
                            text = if (isAudioPlayingThisEra) (if (currentLanguage == AppLanguage.ARABIC) "مشتغل" else "Playing") else (if (currentLanguage == AppLanguage.ARABIC) "صوتيات" else "Audio"),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EraCardDetailed(
    status: EraCompletionStatus,
    currentLanguage: AppLanguage,
    audioState: CurrentAudioState,
    onClick: () -> Unit,
    onExplore: () -> Unit,
    onStartQuiz: () -> Unit,
    onPlayAudio: () -> Unit
) {
    val era = status.era
    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val eraDesc = if (currentLanguage == AppLanguage.ARABIC) era.descriptionAr else era.descriptionEn
    val isAudioPlayingThisEra = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("era_card_detailed_${era.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (status.isMastered) MalachiteGreen else if (status.isStarted) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (status.isMastered) MalachiteGreen.copy(alpha = 0.2f) else PharaohGold.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (status.isMastered) MalachiteGreen else PharaohGold),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(era.iconEmoji, fontSize = 20.sp)
                        }
                    }

                    Column {
                        Text(
                            text = eraTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = eraPeriod,
                            style = MaterialTheme.typography.bodySmall,
                            color = PharaohGoldDark,
                            fontSize = 11.sp
                        )
                    }
                }

                EraStatusBadge(status = status, currentLanguage = currentLanguage)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = eraDesc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Metrics & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${Localization.get("era_overall_progress", currentLanguage)} ${status.overallPercentage}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (status.isMastered) MalachiteGreen else PharaohGold
                        )
                        Text(
                            text = "📖 ${status.completedSectionsCount}/${status.totalSections}  🎯 ${if (status.quizPercentage > 0) "${status.quizPercentage}%" else "-"}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = { (status.overallPercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (status.isMastered) MalachiteGreen else PharaohGold,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Audio Narration Button
                    Button(
                        onClick = onPlayAudio,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("era_audio_btn_${era.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAudioPlayingThisEra) MalachiteGreen else PharaohGold.copy(alpha = 0.25f),
                            contentColor = if (isAudioPlayingThisEra) Color.Black else PharaohGold
                        ),
                        border = if (!isAudioPlayingThisEra) androidx.compose.foundation.BorderStroke(1.dp, PharaohGold) else null
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(if (isAudioPlayingThisEra) "⏸️" else "🎙️", fontSize = 11.sp)
                            Text(
                                text = if (isAudioPlayingThisEra) (if (currentLanguage == AppLanguage.ARABIC) "مشتغل" else "Playing") else (if (currentLanguage == AppLanguage.ARABIC) "صوتيات" else "Audio"),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onExplore,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                    ) {
                        Text(
                            Localization.get("era_explore_btn", currentLanguage),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )
                    }

                    Button(
                        onClick = onStartQuiz,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (status.isMastered) MalachiteGreen else PharaohGold,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            Localization.get("era_quiz_btn", currentLanguage),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EraStatusBadge(
    status: EraCompletionStatus,
    currentLanguage: AppLanguage
) {
    val (badgeText, badgeColor) = when {
        status.isMastered -> Pair(Localization.get("era_mastered_badge", currentLanguage), MalachiteGreen)
        status.isStarted -> Pair(Localization.get("era_in_progress_badge", currentLanguage), PharaohGold)
        else -> Pair(Localization.get("era_not_started_badge", currentLanguage), MaterialTheme.colorScheme.onSurfaceVariant)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = badgeColor.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(0.8.dp, badgeColor.copy(alpha = 0.5f))
    ) {
        Text(
            text = badgeText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = badgeColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun EraDetailDialog(
    status: EraCompletionStatus,
    currentLanguage: AppLanguage,
    audioState: CurrentAudioState,
    onDismiss: () -> Unit,
    onExploreSection: (CivilizationSection) -> Unit,
    onStartQuizSection: (CivilizationSection) -> Unit,
    onPlayAudio: (HistoricalEra) -> Unit,
    onOpenEraDetail: (HistoricalEra) -> Unit = {}
) {
    val era = status.era
    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val eraDesc = if (currentLanguage == AppLanguage.ARABIC) era.descriptionAr else era.descriptionEn
    val isAudioPlayingThisEra = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING

    val includedSections = CivilizationData.sections.filter { it.id in era.sectionIds }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("era_details_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Top close button and title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(era.iconEmoji, fontSize = 28.sp)
                        Column {
                            Text(
                                text = eraTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = eraPeriod,
                                style = MaterialTheme.typography.bodySmall,
                                color = PharaohGoldDark
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("era_dialog_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = PharaohGold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = Localization.get("era_overall_progress", currentLanguage),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${status.overallPercentage}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (status.isMastered) MalachiteGreen else PharaohGold
                            )
                        }

                        EraStatusBadge(status = status, currentLanguage = currentLanguage)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Audio Narration Banner
                if (era.audioNarration != null) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PharaohGold.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlayAudio(era) }
                            .testTag("dialog_era_audio_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = PharaohGold,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(if (isAudioPlayingThisEra) "⏸️" else "🎙️", fontSize = 16.sp)
                                    }
                                }

                                Column {
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) era.audioNarration.titleAr else era.audioNarration.titleEn,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${if (currentLanguage == AppLanguage.ARABIC) "شرح صوتي موثق" else "Documented Audio"} • ${era.audioNarration.durationSeconds}s",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PharaohGoldDark
                                    )
                                }
                            }

                            Button(
                                onClick = { onPlayAudio(era) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isAudioPlayingThisEra) MalachiteGreen else PharaohGold,
                                    contentColor = Color.Black
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = if (isAudioPlayingThisEra) (if (currentLanguage == AppLanguage.ARABIC) "إيقاف" else "Pause") else (if (currentLanguage == AppLanguage.ARABIC) "استمع" else "Listen"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = eraDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Full Historical Era Details Button
                Button(
                    onClick = {
                        onDismiss()
                        onOpenEraDetail(era)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharaohGold,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("tracker_dialog_open_era_detail_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "عرض التفاصيل التاريخية الكاملة لهذا العصر 📜" else "Explore Full Historical Era Details 📜",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = Localization.get("era_sections_included", currentLanguage),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PharaohGold
                )

                Spacer(modifier = Modifier.height(8.dp))

                includedSections.forEach { section ->
                    val isRead = section.id in status.era.sectionIds && status.completedSectionsCount > 0
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(section.iconEmoji, fontSize = 20.sp)
                                Column {
                                    Text(
                                        text = section.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${section.readTimeMinutes} ${Localization.get("minutes_read", currentLanguage)} • ${section.quiz.size} ${Localization.get("questions_count_suffix", currentLanguage)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = { onExploreSection(section) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = "قراءة",
                                        tint = PharaohGold
                                    )
                                }

                                IconButton(
                                    onClick = { onStartQuizSection(section) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "امتحان",
                                        tint = if (status.passedQuizzesCount > 0) MalachiteGreen else PharaohGold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
