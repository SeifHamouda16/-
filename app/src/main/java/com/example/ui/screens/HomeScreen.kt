package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.model.AudioPlaybackState
import com.example.model.CurrentAudioState
import com.example.model.HistoricalEra
import com.example.ui.components.EraEntryPointsComponent
import com.example.ui.components.EraMiniAudioBar
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.CivilizationSection
import com.example.model.SectionQuizResult
import com.example.model.UserProfile
import com.example.ui.components.EraProgressTracker
import com.example.ui.theme.MalachiteGreen
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

@Composable
fun HomeScreen(
    sections: List<CivilizationSection>,
    completedSections: Set<String>,
    quizScores: Map<String, SectionQuizResult>,
    bookmarkedSections: Set<String>,
    userTitle: Pair<String, String>,
    totalPoints: Int,
    currentLanguage: AppLanguage,
    userProfile: UserProfile,
    searchQuery: String,
    showOnlyBookmarked: Boolean,
    onSearchChange: (String) -> Unit,
    onToggleBookmarkFilter: () -> Unit,
    onSectionClick: (CivilizationSection) -> Unit,
    onStartQuizClick: (CivilizationSection) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onStartGrandExam: () -> Unit,
    onOpenAccountSettings: () -> Unit,
    onToggleTheme: () -> Unit = {},
    onCycleThemeColor: () -> Unit = {},
    isDarkTheme: Boolean = true,
    audioState: CurrentAudioState = CurrentAudioState(),
    onPlayEraAudio: (HistoricalEra) -> Unit = {},
    onToggleEraAudio: () -> Unit = {},
    onExpandAudioPlayer: () -> Unit = {},
    onOpenEraDetail: (HistoricalEra) -> Unit = {},
    onOpenWelcome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Banner Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_egypt),
                    contentDescription = "بانوراما حضارة مصر القديمة",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    NileLapisDark.copy(alpha = 0.5f),
                                    NileLapisDark.copy(alpha = 0.65f),
                                    NileLapisDark.copy(alpha = 0.95f)
                                )
                            )
                        )
                )

                // Top bar in hero with Day/Night (Theme) toggle and Profile/Login button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // علامة الليل والنهار (Day and Night Toggle)
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = NileLapisDark.copy(alpha = 0.85f),
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold),
                            modifier = Modifier
                                .clickable { onToggleTheme() }
                                .testTag("home_theme_toggle_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = if (isDarkTheme) "🌙" else "☀️",
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (isDarkTheme) {
                                        if (currentLanguage == AppLanguage.ARABIC) "الليل" else "Night"
                                    } else {
                                        if (currentLanguage == AppLanguage.ARABIC) "النهار" else "Day"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PharaohGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // زر المظهر الملكي الفرعوني
                        Surface(
                            shape = CircleShape,
                            color = NileLapisDark.copy(alpha = 0.85f),
                            border = androidx.compose.foundation.BorderStroke(1.4.dp, PharaohGold),
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { onCycleThemeColor() }
                                .testTag("home_colors_palette_button")
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "🎨",
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    // Profile / Sign-in Pill Button (Core Requirement)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = NileLapisDark.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold),
                        modifier = Modifier
                            .clickable { onOpenAccountSettings() }
                            .testTag("home_profile_avatar_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(userProfile.avatarEmoji, fontSize = 16.sp)
                            Text(
                                text = if (userProfile.isLoggedIn) userProfile.name else Localization.get("login_card_title", currentLanguage),
                                style = MaterialTheme.typography.labelSmall,
                                color = PharaohGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Bottom Content in Hero
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = Localization.get("welcome_greeting", currentLanguage),
                        style = MaterialTheme.typography.titleMedium,
                        color = PharaohGoldLight,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = Localization.get("app_title", currentLanguage),
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = Localization.get("welcome_hint", currentLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PharaohGold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onOpenWelcome() }
                            .testTag("home_open_welcome_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("📜", fontSize = 14.sp)
                            Text(
                                text = Localization.get("welcome_menu_item", currentLanguage),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        // User Pharaonic Title & Stats Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("user_stats_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = Localization.get("user_rank_label", currentLanguage),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = userTitle.first,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                            Text(
                                text = userTitle.second,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PharaohGold.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.EmojiEvents,
                                    contentDescription = "النقاط",
                                    tint = PharaohGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "$totalPoints ${Localization.get("points_label", currentLanguage)}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val totalSec = 6
                    val completedCount = completedSections.size
                    val passedQuizCount = quizScores.values.count { it.passed }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(
                            label = Localization.get("completed_sections", currentLanguage),
                            value = "$completedCount / $totalSec",
                            icon = Icons.Default.CheckCircle,
                            tint = MalachiteGreen
                        )
                        StatItem(
                            label = Localization.get("passed_quizzes", currentLanguage),
                            value = "$passedQuizCount / $totalSec",
                            icon = Icons.Default.Quiz,
                            tint = PharaohGold
                        )
                        StatItem(
                            label = Localization.get("achievement_ratio", currentLanguage),
                            value = "${(passedQuizCount * 100) / totalSec}%",
                            icon = Icons.Default.WorkspacePremium,
                            tint = PharaohGoldDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { (passedQuizCount.toFloat() / totalSec.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = PharaohGold,
                        trackColor = PharaohGold.copy(alpha = 0.2f)
                    )
                }
            }
        }

        // Mini Audio Bar if era narration is active
        if (audioState.state in listOf(AudioPlaybackState.PLAYING, AudioPlaybackState.PAUSED)) {
            item {
                EraMiniAudioBar(
                    audioState = audioState,
                    currentLanguage = currentLanguage,
                    onPlayPause = onToggleEraAudio,
                    onExpand = onExpandAudioPlayer
                )
            }
        }

        // Historical Eras Entry Points Component (Material 3 Entry Gateways)
        item {
            EraEntryPointsComponent(
                completedSections = completedSections,
                quizScores = quizScores,
                currentLanguage = currentLanguage,
                onEnterEra = { _, sec -> onSectionClick(sec) },
                onStartQuizClick = onStartQuizClick,
                onPlayEraAudio = onPlayEraAudio,
                onOpenEraDetail = onOpenEraDetail,
                audioState = audioState
            )
        }

        // Historical Eras Progress Tracker Component (Core Requirement)
        item {
            EraProgressTracker(
                completedSections = completedSections,
                quizScores = quizScores,
                currentLanguage = currentLanguage,
                onSectionClick = onSectionClick,
                onStartQuizClick = onStartQuizClick,
                onPlayAudio = onPlayEraAudio,
                onOpenEraDetail = onOpenEraDetail,
                audioState = audioState
            )
        }

        // Grand Exam Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onStartGrandExam() }
                    .testTag("grand_exam_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NileLapisDark
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PharaohGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦅", fontSize = 24.sp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localization.get("grand_exam_banner_title", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )
                        Text(
                            text = Localization.get("grand_exam_banner_desc", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Button(
                        onClick = onStartGrandExam,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PharaohGold,
                            contentColor = NileLapisDark
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(Localization.get("take_grand_exam_btn", currentLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Search & Filters
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    placeholder = { Text(Localization.get("search_placeholder", currentLanguage)) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "بحث", tint = PharaohGold)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "مسح")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PharaohGold,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !showOnlyBookmarked,
                        onClick = { if (showOnlyBookmarked) onToggleBookmarkFilter() },
                        label = { Text("${Localization.get("filter_all", currentLanguage)} (${sections.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PharaohGold.copy(alpha = 0.2f),
                            selectedLabelColor = PharaohGold
                        )
                    )

                    FilterChip(
                        selected = showOnlyBookmarked,
                        onClick = onToggleBookmarkFilter,
                        label = { Text("${Localization.get("filter_saved", currentLanguage)} (${bookmarkedSections.size})") },
                        leadingIcon = {
                            Icon(
                                if (showOnlyBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PharaohGold.copy(alpha = 0.2f),
                            selectedLabelColor = PharaohGold
                        )
                    )
                }
            }
        }

        // Section list title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = Localization.get("sections_header", currentLanguage),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = Localization.get("sections_badge_hint", currentLanguage),
                    style = MaterialTheme.typography.labelMedium,
                    color = PharaohGold
                )
            }
        }

        // Sections List
        items(sections, key = { it.id }) { section ->
            val isCompleted = completedSections.contains(section.id)
            val quizResult = quizScores[section.id]
            val isBookmarked = bookmarkedSections.contains(section.id)

            SectionCard(
                section = section,
                isCompleted = isCompleted,
                quizResult = quizResult,
                isBookmarked = isBookmarked,
                currentLanguage = currentLanguage,
                onCardClick = { onSectionClick(section) },
                onStartQuiz = { onStartQuizClick(section) },
                onToggleBookmark = { onToggleBookmark(section.id) }
            )
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun SectionCard(
    section: CivilizationSection,
    isCompleted: Boolean,
    quizResult: SectionQuizResult?,
    isBookmarked: Boolean,
    currentLanguage: AppLanguage,
    onCardClick: () -> Unit,
    onStartQuiz: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCardClick() }
            .testTag("section_card_${section.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (quizResult?.passed == true) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column {
            // Header Image with badge & bookmark
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = section.drawableResId),
                    contentDescription = section.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )

                // Top row: Era badge + Bookmark icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NileLapisDark.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, PharaohGold)
                    ) {
                        Text(
                            text = section.era,
                            style = MaterialTheme.typography.labelSmall,
                            color = PharaohGoldLight,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "حفظ",
                            tint = if (isBookmarked) PharaohGold else Color.White
                        )
                    }
                }

                // Bottom row in Image: Title & Icon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = section.iconEmoji, fontSize = 24.sp)
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Card Body
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = section.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = PharaohGold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = section.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Status Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Reading status chip
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isCompleted) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MalachiteGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = Localization.get("completed_tag", currentLanguage),
                                style = MaterialTheme.typography.labelSmall,
                                color = MalachiteGreen,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "⏱️ ${section.readTimeMinutes} ${Localization.get("minutes_read", currentLanguage)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Quiz status
                    if (quizResult != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (quizResult.passed) PharaohGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = "${Localization.get("quiz_score_prefix", currentLanguage)}${quizResult.percentage}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (quizResult.passed) PharaohGold else MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "💡 ${section.quiz.size} ${Localization.get("questions_count_suffix", currentLanguage)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons Row (Read & Quiz)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onCardClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(Localization.get("read_and_learn_btn", currentLanguage), fontSize = 13.sp)
                    }

                    Button(
                        onClick = onStartQuiz,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("start_quiz_button_${section.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PharaohGold,
                            contentColor = NileLapisDark
                        )
                    ) {
                        Icon(
                            Icons.Default.Quiz,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = NileLapisDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (quizResult != null) Localization.get("retake_quiz_btn", currentLanguage) else Localization.get("start_quiz_btn", currentLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
