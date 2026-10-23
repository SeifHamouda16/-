package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

enum class EraDisplayMode {
    CARDS,
    TIMELINE
}

/**
 * Material3 Home Screen Component listing historical eras of Ancient Egypt as entry points.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EraEntryPointsComponent(
    completedSections: Set<String>,
    quizScores: Map<String, SectionQuizResult>,
    currentLanguage: AppLanguage,
    onEnterEra: (HistoricalEra, CivilizationSection) -> Unit,
    onStartQuizClick: (CivilizationSection) -> Unit,
    onPlayEraAudio: (HistoricalEra) -> Unit = {},
    onOpenEraDetail: (HistoricalEra) -> Unit = {},
    audioState: CurrentAudioState = CurrentAudioState(),
    modifier: Modifier = Modifier
) {
    val eraStatuses = remember(completedSections, quizScores) {
        HistoricalErasData.calculateErasProgress(completedSections, quizScores)
    }

    var selectedEraIdFilter by remember { mutableStateOf<String?>(null) }
    var displayMode by remember { mutableStateOf(EraDisplayMode.CARDS) }
    var selectedEraForGateway by remember { mutableStateOf<EraCompletionStatus?>(null) }

    val displayedEras = remember(selectedEraIdFilter, eraStatuses) {
        if (selectedEraIdFilter == null) eraStatuses
        else eraStatuses.filter { it.era.id == selectedEraIdFilter }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("era_entry_points_component")
            .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow)),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.2.dp, PharaohGold.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Majestic Title, Icon & Display Mode Switcher
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
                        color = PharaohGold.copy(alpha = 0.18f),
                        border = BorderStroke(1.2.dp, PharaohGold),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Column {
                        Text(
                            text = Localization.get("eras_entry_points_title", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Localization.get("eras_entry_points_subtitle", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Switch between Cards View and Chronological Timeline View
                IconButton(
                    onClick = {
                        displayMode = if (displayMode == EraDisplayMode.CARDS) EraDisplayMode.TIMELINE else EraDisplayMode.CARDS
                    },
                    modifier = Modifier.testTag("era_display_mode_btn")
                ) {
                    Icon(
                        imageVector = if (displayMode == EraDisplayMode.CARDS) Icons.Default.Timeline else Icons.Default.ViewAgenda,
                        contentDescription = if (displayMode == EraDisplayMode.CARDS) Localization.get("era_timeline_mode", currentLanguage) else Localization.get("era_cards_mode", currentLanguage),
                        tint = PharaohGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Horizontal Filter Chips Row: All Eras + Era Shortcuts
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedEraIdFilter == null,
                        onClick = { selectedEraIdFilter = null },
                        label = { Text(Localization.get("era_filter_all", currentLanguage), fontSize = 11.sp) },
                        leadingIcon = {
                            if (selectedEraIdFilter == null) {
                                Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PharaohGold,
                            selectedLabelColor = Color.Black,
                            selectedLeadingIconColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, if (selectedEraIdFilter == null) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.testTag("era_filter_chip_all")
                    )
                }

                items(eraStatuses, key = { it.era.id }) { status ->
                    val isSelected = selectedEraIdFilter == status.era.id
                    val eraName = if (currentLanguage == AppLanguage.ARABIC) status.era.nameAr else status.era.nameEn

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedEraIdFilter = if (isSelected) null else status.era.id
                        },
                        label = {
                            Text(
                                text = "${status.era.iconEmoji} ${eraName.split(" ").take(2).joinToString(" ")}",
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PharaohGold,
                            selectedLabelColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, if (isSelected) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.testTag("era_filter_chip_${status.era.id}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Body: Render either Gateway Cards or Chronological Timeline
            when (displayMode) {
                EraDisplayMode.CARDS -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        displayedEras.forEach { status ->
                            EraEntryPointCard(
                                status = status,
                                currentLanguage = currentLanguage,
                                audioState = audioState,
                                onEnter = {
                                    onOpenEraDetail(status.era)
                                },
                                onStartQuiz = {
                                    val section = CivilizationData.sections.find { it.id in status.era.sectionIds }
                                    if (section != null) onStartQuizClick(section)
                                },
                                onPlayAudio = { onPlayEraAudio(status.era) },
                                onOpenGateway = { selectedEraForGateway = status }
                            )
                        }
                    }
                }

                EraDisplayMode.TIMELINE -> {
                    EraTimelineView(
                        eras = displayedEras,
                        currentLanguage = currentLanguage,
                        audioState = audioState,
                        onEnter = { era ->
                            onOpenEraDetail(era)
                        },
                        onPlayAudio = { onPlayEraAudio(it) },
                        onOpenGateway = { selectedEraForGateway = it }
                    )
                }
            }
        }
    }

    // Era Gateway Modal Dialog when requested
    if (selectedEraForGateway != null) {
        val status = selectedEraForGateway!!
        EraGatewayDialog(
            status = status,
            currentLanguage = currentLanguage,
            audioState = audioState,
            onDismiss = { selectedEraForGateway = null },
            onEnterSection = { section ->
                selectedEraForGateway = null
                onEnterEra(status.era, section)
            },
            onStartQuiz = { section ->
                selectedEraForGateway = null
                onStartQuizClick(section)
            },
            onPlayAudio = { onPlayEraAudio(status.era) },
            onOpenEraDetail = { era ->
                selectedEraForGateway = null
                onOpenEraDetail(era)
            }
        )
    }
}

/**
 * An individual Historical Era Card serving as a primary entry point into Ancient Egypt.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EraEntryPointCard(
    status: EraCompletionStatus,
    currentLanguage: AppLanguage,
    audioState: CurrentAudioState,
    onEnter: () -> Unit,
    onStartQuiz: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenGateway: () -> Unit
) {
    val era = status.era
    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val eraDesc = if (currentLanguage == AppLanguage.ARABIC) era.descriptionAr else era.descriptionEn
    val landmarks = if (currentLanguage == AppLanguage.ARABIC) era.landmarksAr else era.landmarksEn

    val isAudioPlayingThisEra = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING
    val primarySection = CivilizationData.sections.find { it.id in era.sectionIds }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("era_entry_card_${era.id}")
            .clickable { onOpenGateway() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (status.isMastered) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.2.dp,
            if (status.isMastered || status.isStarted) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Icon, Era Title & Period, Status Badge
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
                        color = PharaohGold.copy(alpha = 0.18f),
                        border = BorderStroke(1.2.dp, PharaohGold),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(era.iconEmoji, fontSize = 24.sp)
                        }
                    }

                    Column {
                        Text(
                            text = eraTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = PharaohGoldDark,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = eraPeriod,
                                style = MaterialTheme.typography.bodySmall,
                                color = PharaohGoldDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Mastery Status Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (status.isMastered || status.isStarted) PharaohGold.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        0.8.dp,
                        if (status.isMastered || status.isStarted) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = if (status.isMastered) Localization.get("era_mastered_badge", currentLanguage)
                        else if (status.isStarted) Localization.get("era_in_progress_badge", currentLanguage)
                        else Localization.get("era_not_started_badge", currentLanguage),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (status.isMastered || status.isStarted) PharaohGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Era Historical Summary
            Text(
                text = eraDesc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Landmarks Chips (Entry Point context highlights)
            if (landmarks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    landmarks.take(3).forEach { landmark ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text("✦", fontSize = 9.sp, color = PharaohGold)
                                Text(
                                    text = landmark,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress & Section Info Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = PharaohGold
                    )
                    Text(
                        text = primarySection?.title ?: eraTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 160.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${status.overallPercentage}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PharaohGold
                    )

                    // Audio Narration Button
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PharaohGold.copy(alpha = 0.2f),
                        border = BorderStroke(0.8.dp, PharaohGold),
                        modifier = Modifier
                            .clickable { onPlayAudio() }
                            .testTag("era_audio_button_${era.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(if (isAudioPlayingThisEra) "⏸️" else "🎙️", fontSize = 10.sp)
                            Text(
                                text = if (isAudioPlayingThisEra) (if (currentLanguage == AppLanguage.ARABIC) "مشتغل" else "Playing")
                                else (if (currentLanguage == AppLanguage.ARABIC) "صوتيات" else "Audio"),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAudioPlayingThisEra) Color.Black else PharaohGold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (status.overallPercentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = PharaohGold,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Action Buttons: Enter Era (Primary Entry Point) & Quiz
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Secondary Action: Quiz
                OutlinedButton(
                    onClick = onStartQuiz,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, PharaohGold),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("era_quiz_button_${era.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Quiz,
                        contentDescription = null,
                        tint = PharaohGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = Localization.get("era_quiz_btn", currentLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                }

                // Primary Entry Point Button
                Button(
                    onClick = onEnter,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PharaohGold,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("era_enter_button_${era.id}")
                ) {
                    Text(
                        text = Localization.get("enter_era_portal", currentLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Chronological Timeline View of the Historical Eras of Ancient Egypt.
 */
@Composable
private fun EraTimelineView(
    eras: List<EraCompletionStatus>,
    currentLanguage: AppLanguage,
    audioState: CurrentAudioState,
    onEnter: (HistoricalEra) -> Unit,
    onPlayAudio: (HistoricalEra) -> Unit,
    onOpenGateway: (EraCompletionStatus) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("era_timeline_view")
    ) {
        eras.forEachIndexed { index, status ->
            val isLast = index == eras.size - 1
            val era = status.era
            val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
            val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
            val isAudioPlaying = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenGateway(status) }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Timeline Indicator Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(36.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PharaohGold,
                        border = BorderStroke(2.dp, PharaohGoldLight),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(era.iconEmoji, fontSize = 14.sp)
                        }
                    }

                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(56.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(PharaohGold, PharaohGoldDark.copy(alpha = 0.4f))
                                    )
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Timeline Card Content
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = if (isLast) 4.dp else 10.dp)
                        .testTag("timeline_era_card_${era.id}"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(0.8.dp, PharaohGold.copy(alpha = 0.4f)),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = eraTitle,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = eraPeriod,
                                style = MaterialTheme.typography.labelSmall,
                                color = PharaohGoldDark,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Audio button
                            IconButton(
                                onClick = { onPlayAudio(era) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text(if (isAudioPlaying) "⏸️" else "🎙️", fontSize = 14.sp)
                            }

                            // Enter Arrow Button
                            Button(
                                onClick = { onEnter(era) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PharaohGold, contentColor = Color.Black),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "دخول" else "Enter",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Era Gateway Modal Dialog providing a majestic overview of the era before entering.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EraGatewayDialog(
    status: EraCompletionStatus,
    currentLanguage: AppLanguage,
    audioState: CurrentAudioState,
    onDismiss: () -> Unit,
    onEnterSection: (CivilizationSection) -> Unit,
    onStartQuiz: (CivilizationSection) -> Unit,
    onPlayAudio: () -> Unit,
    onOpenEraDetail: (HistoricalEra) -> Unit = {}
) {
    val era = status.era
    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val eraDesc = if (currentLanguage == AppLanguage.ARABIC) era.descriptionAr else era.descriptionEn
    val landmarks = if (currentLanguage == AppLanguage.ARABIC) era.landmarksAr else era.landmarksEn
    val sections = CivilizationData.sections.filter { it.id in era.sectionIds }
    val isAudioPlaying = audioState.era?.id == era.id && audioState.state == AudioPlaybackState.PLAYING

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("era_gateway_dialog"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, PharaohGold)
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PharaohGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.2.dp, PharaohGold),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(era.iconEmoji, fontSize = 24.sp)
                            }
                        }

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
                        modifier = Modifier.testTag("gateway_close_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = PharaohGold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Era Narrative
                Text(
                    text = eraDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                // Landmarks
                if (landmarks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = Localization.get("era_landmarks_label", currentLanguage),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        landmarks.forEach { landmark ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(0.6.dp, PharaohGold.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "🏛️ $landmark",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Audio Guide Bar
                if (era.audioNarration != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PharaohGold.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, PharaohGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlayAudio() }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(if (isAudioPlaying) "⏸️" else "🎙️", fontSize = 18.sp)
                                Column {
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) era.audioNarration.titleAr else era.audioNarration.titleEn,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${era.audioNarration.durationSeconds} ${if (currentLanguage == AppLanguage.ARABIC) "ثانية من الشرح الموثق" else "seconds narration"}",
                                        fontSize = 10.sp,
                                        color = PharaohGoldDark
                                    )
                                }
                            }

                            Text(
                                text = if (isAudioPlaying) (if (currentLanguage == AppLanguage.ARABIC) "إيقاف" else "Pause")
                                else (if (currentLanguage == AppLanguage.ARABIC) "استمع" else "Listen"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary Button to open the full Historical Era Detail Screen
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
                        .testTag("dialog_open_era_detail_screen_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
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

                Spacer(modifier = Modifier.height(14.dp))

                // Included Section / Gateway Entry
                sections.forEach { section ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
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
                                Text(section.iconEmoji, fontSize = 22.sp)
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

                            Button(
                                onClick = { onEnterSection(section) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PharaohGold, contentColor = Color.Black),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("gateway_enter_btn_${section.id}")
                            ) {
                                Text(
                                    text = Localization.get("read_and_learn_btn", currentLanguage),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
