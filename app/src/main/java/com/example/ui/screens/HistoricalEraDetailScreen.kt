package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.AudioPlaybackState
import com.example.model.CivilizationSection
import com.example.model.CurrentAudioState
import com.example.model.EraHistoricalMilestone
import com.example.model.EraProminentFigure
import com.example.model.HistoricalEra
import com.example.model.SectionQuizResult
import com.example.ui.components.AudioWaveformVisualizer
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoricalEraDetailScreen(
    era: HistoricalEra,
    completedSections: Set<String>,
    quizScores: Map<String, SectionQuizResult>,
    audioState: CurrentAudioState,
    currentLanguage: AppLanguage,
    onNavigateBack: () -> Unit,
    onSelectEra: (HistoricalEra) -> Unit,
    onOpenSection: (CivilizationSection) -> Unit,
    onStartQuiz: (CivilizationSection) -> Unit,
    onPlayAudio: (HistoricalEra) -> Unit,
    onToggleAudioPlayback: () -> Unit,
    onSeekAudio: (Int) -> Unit,
    onSetAudioSpeed: (Float) -> Unit,
    onToggleAudioMute: () -> Unit,
    onToggleTheme: () -> Unit = {},
    isDarkTheme: Boolean = true,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val eraTitle = if (currentLanguage == AppLanguage.ARABIC) era.nameAr else era.nameEn
    val eraPeriod = if (currentLanguage == AppLanguage.ARABIC) era.periodAr else era.periodEn
    val eraDesc = if (currentLanguage == AppLanguage.ARABIC) era.descriptionAr else era.descriptionEn
    val capital = if (currentLanguage == AppLanguage.ARABIC) era.capitalCityAr else era.capitalCityEn
    val dynasties = if (currentLanguage == AppLanguage.ARABIC) era.dynastiesAr else era.dynastiesEn
    val landmarks = if (currentLanguage == AppLanguage.ARABIC) era.landmarksAr else era.landmarksEn

    val includedSections = remember(era.id) {
        CivilizationData.sections.filter { it.id in era.sectionIds }
    }

    val status = remember(era.id, completedSections, quizScores) {
        HistoricalErasData.calculateErasProgress(completedSections, quizScores).find { it.era.id == era.id }
    }

    val isAudioForThisEra = audioState.era?.id == era.id
    val isPlaying = isAudioForThisEra && audioState.state == AudioPlaybackState.PLAYING
    val isCompleted = isAudioForThisEra && audioState.state == AudioPlaybackState.COMPLETED
    val progressRatio = if (isAudioForThisEra && audioState.totalDurationSeconds > 0) {
        audioState.currentPositionSeconds.toFloat() / audioState.totalDurationSeconds.toFloat()
    } else 0f

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = eraTitle,
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
                .testTag("historical_era_detail_screen"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Horizontal Era Quick-Switcher (Browse any of the 6 eras directly)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "تصفح عصور مصر القديمة:" else "Browse Ancient Egyptian Eras:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(HistoricalErasData.eras, key = { it.id }) { itemEra ->
                            val isSelected = itemEra.id == era.id
                            val itemName = if (currentLanguage == AppLanguage.ARABIC) itemEra.nameAr else itemEra.nameEn

                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectEra(itemEra) },
                                label = {
                                    Text(
                                        text = "${itemEra.iconEmoji} ${itemName.split(" ").take(2).joinToString(" ")}",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PharaohGold,
                                    selectedLabelColor = Color.Black
                                ),
                                border = BorderStroke(1.dp, if (isSelected) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                modifier = Modifier.testTag("era_switch_chip_${itemEra.id}")
                            )
                        }
                    }
                }
            }

            // Hero Banner Card for the selected era
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("era_hero_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.2.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Era Big Emblem
                        Surface(
                            shape = CircleShape,
                            color = PharaohGold.copy(alpha = 0.2f),
                            border = BorderStroke(2.dp, PharaohGold),
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = era.iconEmoji, fontSize = 36.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = eraTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Period & Dynasty Pills
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = eraPeriod,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGoldDark
                            )
                        }

                        if (dynasties.isNotBlank() || capital.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (dynasties.isNotBlank()) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = "👑 $dynasties",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                if (capital.isNotBlank()) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = "🏛️ $capital",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Indicator
                        if (status != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${Localization.get("era_overall_progress", currentLanguage)} ${status.overallPercentage}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = PharaohGold.copy(alpha = 0.15f),
                                    border = BorderStroke(0.8.dp, PharaohGold)
                                ) {
                                    Text(
                                        text = if (status.isMastered) Localization.get("era_mastered_badge", currentLanguage)
                                        else if (status.isStarted) Localization.get("era_in_progress_badge", currentLanguage)
                                        else Localization.get("era_not_started_badge", currentLanguage),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { (status.overallPercentage / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = PharaohGold,
                                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }

            // Interactive Royal Audio Narration Player
            if (era.audioNarration != null) {
                val narration = era.audioNarration
                val audioTitle = if (currentLanguage == AppLanguage.ARABIC) narration.titleAr else narration.titleEn
                val script = if (currentLanguage == AppLanguage.ARABIC) narration.narratorScriptAr else narration.narratorScriptEn

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .testTag("era_audio_player_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                        border = BorderStroke(1.2.dp, PharaohGold)
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
                                    Icon(
                                        imageVector = Icons.Default.Headphones,
                                        contentDescription = null,
                                        tint = PharaohGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "الشرح الصوتي التوضيحي للعصر" else "Documentary Audio Narration",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold
                                    )
                                }

                                IconButton(
                                    onClick = onToggleAudioMute,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (audioState.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                        contentDescription = "كتم الصوت",
                                        tint = if (audioState.isMuted) MaterialTheme.colorScheme.error else PharaohGold
                                    )
                                }
                            }

                            Text(
                                text = audioTitle,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Waveform Visualizer
                            AudioWaveformVisualizer(isPlaying = isPlaying)

                            // Progress Slider
                            Slider(
                                value = progressRatio.coerceIn(0f, 1f),
                                onValueChange = { ratio ->
                                    val sec = (ratio * (if (isAudioForThisEra) audioState.totalDurationSeconds else narration.durationSeconds)).toInt()
                                    onSeekAudio(sec)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = SliderDefaults.colors(
                                    thumbColor = PharaohGold,
                                    activeTrackColor = PharaohGold,
                                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )

                            // Controls Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isAudioForThisEra) "${audioState.currentPositionSeconds}s / ${audioState.totalDurationSeconds}s" else "0s / ${narration.durationSeconds}s",
                                    fontSize = 11.sp,
                                    color = PharaohGoldDark,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Rewind
                                    IconButton(
                                        onClick = {
                                            if (isAudioForThisEra) onSeekAudio(audioState.currentPositionSeconds - 10)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.FastRewind, contentDescription = "-10s", tint = PharaohGold)
                                    }

                                    // Main Play Button
                                    Surface(
                                        shape = CircleShape,
                                        color = PharaohGold,
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clickable {
                                                if (isAudioForThisEra) onToggleAudioPlayback()
                                                else onPlayAudio(era)
                                            }
                                            .testTag("era_detail_audio_play_btn")
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = when {
                                                    isCompleted -> Icons.Default.Replay
                                                    isPlaying -> Icons.Default.Pause
                                                    else -> Icons.Default.PlayArrow
                                                },
                                                contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                                                tint = Color.Black,
                                                modifier = Modifier.size(26.dp)
                                            )
                                        }
                                    }

                                    // Forward
                                    IconButton(
                                        onClick = {
                                            if (isAudioForThisEra) onSeekAudio(audioState.currentPositionSeconds + 10)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.FastForward, contentDescription = "+10s", tint = PharaohGold)
                                    }
                                }

                                // Speed Buttons
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    listOf(1.0f, 1.25f).forEach { spd ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (audioState.playbackSpeed == spd) PharaohGold else MaterialTheme.colorScheme.surface,
                                            modifier = Modifier.clickable { onSetAudioSpeed(spd) }
                                        ) {
                                            Text(
                                                text = "${spd}x",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (audioState.playbackSpeed == spd) Color.Black else MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Historical Overview & Narrative
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HistoryEdu,
                                contentDescription = null,
                                tint = PharaohGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "السياق التاريخي والأهمية الحضارية" else "Historical Context & Civilizational Significance",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = eraDesc,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Historical Milestones Timeline (أهم المحطات والأحداث المفصلية)
            if (era.milestones.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "المحطات التاريخية والأحداث المفصلية ⏳" else "Pivotal Historical Milestones ⏳",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(era.milestones) { milestone ->
                    MilestoneCard(milestone = milestone, currentLanguage = currentLanguage)
                }
            }

            // Prominent Pharaohs & Figures (أبرز ملوك وشخصيات العصر)
            if (era.prominentFigures.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "أبرز ملوك وشخصيات هذا العصر 👑" else "Prominent Monarchs & Figures 👑",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(era.prominentFigures) { figure ->
                    FigureCard(figure = figure, currentLanguage = currentLanguage)
                }
            }

            // Major Landmarks & Architectural Wonders
            if (landmarks.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.35f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = PharaohGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "أبرز الشواهد والمعالم الخالدة لهذا العصر" else "Enduring Landmarks of this Era",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                landmarks.forEach { landmark ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(0.8.dp, PharaohGold.copy(alpha = 0.4f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text("🏛️", fontSize = 14.sp)
                                            Text(
                                                text = landmark,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Civilizational Innovations & Achievements
            if (era.achievements.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "الابتكارات والإنجازات الحضارية ⚡" else "Civilizational Innovations ⚡",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(era.achievements) { ach ->
                    val title = if (currentLanguage == AppLanguage.ARABIC) ach.titleAr else ach.titleEn
                    val detail = if (currentLanguage == AppLanguage.ARABIC) ach.detailAr else ach.detailEn

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(0.8.dp, PharaohGold.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(ach.iconEmoji, fontSize = 24.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = detail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Included Lesson Sections (دخول محاور ودروس هذا العصر)
            if (includedSections.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "أقسام ومحاور المعرفة التابعة لهذا العصر 📖" else "Knowledge Lessons Included in this Era 📖",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(includedSections) { sec ->
                    val isSecRead = completedSections.contains(sec.id)
                    val secQuizResult = quizScores[sec.id]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { onOpenSection(sec) }
                            .testTag("era_section_card_${sec.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, PharaohGold)
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
                                    Text(sec.iconEmoji, fontSize = 24.sp)
                                    Column {
                                        Text(
                                            text = sec.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${sec.readTimeMinutes} ${Localization.get("minutes_read", currentLanguage)} • ${sec.quiz.size} ${Localization.get("questions_count_suffix", currentLanguage)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = PharaohGold.copy(alpha = 0.15f),
                                    border = BorderStroke(0.8.dp, PharaohGold)
                                ) {
                                    Text(
                                        text = if (isSecRead) Localization.get("completed_tag", currentLanguage)
                                        else Localization.get("learning_in_progress", currentLanguage),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onStartQuiz(sec) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, PharaohGold),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp), tint = PharaohGold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (secQuizResult != null) "${secQuizResult.percentage}%" else Localization.get("start_quiz_btn", currentLanguage),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold
                                    )
                                }

                                Button(
                                    onClick = { onOpenSection(sec) },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PharaohGold, contentColor = Color.Black),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = Localization.get("read_and_learn_btn", currentLanguage),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MilestoneCard(
    milestone: EraHistoricalMilestone,
    currentLanguage: AppLanguage
) {
    val title = if (currentLanguage == AppLanguage.ARABIC) milestone.titleAr else milestone.titleEn
    val desc = if (currentLanguage == AppLanguage.ARABIC) milestone.descriptionAr else milestone.descriptionEn
    val year = if (currentLanguage == AppLanguage.ARABIC) milestone.yearAr else milestone.yearEn

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.8.dp, PharaohGold.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PharaohGold.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, PharaohGold),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = year,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PharaohGold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun FigureCard(
    figure: EraProminentFigure,
    currentLanguage: AppLanguage
) {
    val name = if (currentLanguage == AppLanguage.ARABIC) figure.nameAr else figure.nameEn
    val role = if (currentLanguage == AppLanguage.ARABIC) figure.roleAr else figure.roleEn
    val ach = if (currentLanguage == AppLanguage.ARABIC) figure.achievementsAr else figure.achievementsEn

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.8.dp, PharaohGold.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = PharaohGold.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, PharaohGold),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(figure.avatarEmoji, fontSize = 22.sp)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = PharaohGold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = ach,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
