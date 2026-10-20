package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.RoyalThemeColor
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

/**
 * Attractive, immersive Welcome & Onboarding Screen for ancient Egypt civilization app.
 * Displays the prestigious app logo, a rich concise overview of ancient Egyptian history,
 * key highlights of the app, language toggling, and a royal call-to-action button.
 */
@Composable
fun WelcomeScreen(
    currentLanguage: AppLanguage,
    isDarkTheme: Boolean,
    royalThemeColor: RoyalThemeColor,
    canNavigateBack: Boolean,
    onStartJourney: () -> Unit,
    onNavigateBack: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onToggleTheme: () -> Unit,
    onCycleThemeColor: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        if (canNavigateBack) {
            onNavigateBack()
        } else {
            onStartJourney()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface,
                        NileLapisDark.copy(alpha = if (isDarkTheme) 0.6f else 0.15f)
                    )
                )
            )
            .testTag("welcome_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Controls Bar (Language Switcher, Theme & Color Cycle, and Optional Back Button)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (canNavigateBack) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                                .border(1.dp, PharaohGold.copy(alpha = 0.5f), CircleShape)
                                .testTag("welcome_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = PharaohGold
                            )
                        }
                    } else {
                        // Pharaoh Symbol Decorative Pill
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = NileLapisDark.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.6f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("☥", fontSize = 16.sp, color = PharaohGold)
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "مصر القديمة" else "Ancient Egypt",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PharaohGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Action buttons (Language, Theme Mode, Color Theme)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Language Switcher
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.7f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    val nextLang = if (currentLanguage == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
                                    onLanguageChange(nextLang)
                                }
                                .testTag("welcome_language_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(if (currentLanguage == AppLanguage.ARABIC) "🇸🇦" else "🇬🇧", fontSize = 14.sp)
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "English" else "العربية",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = PharaohGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Day/Night toggle
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .clickable { onToggleTheme() }
                                .testTag("welcome_theme_mode_toggle")
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text(if (isDarkTheme) "🌙" else "☀️", fontSize = 16.sp)
                            }
                        }

                        // Color theme cycle button
                        Surface(
                            shape = CircleShape,
                            color = NileLapisDark,
                            border = BorderStroke(1.4.dp, PharaohGold),
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .clickable { onCycleThemeColor() }
                                .testTag("welcome_color_theme_toggle")
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Text("🎨", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }

            // App Logo Section with Glowing Halo
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(140.dp)
                            .shadow(16.dp, CircleShape, spotColor = PharaohGold, ambientColor = PharaohGold)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        NileLapisDark,
                                        Color(0xFF0A0E1A)
                                    )
                                )
                            )
                            .border(BorderStroke(3.dp, PharaohGold), CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo_egypt),
                            contentDescription = Localization.get("welcome_screen_title", currentLanguage),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Royal Welcome Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PharaohGold.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.8f)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = Localization.get("welcome_badge", currentLanguage),
                            style = MaterialTheme.typography.labelMedium,
                            color = PharaohGold,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    // App Title
                    Text(
                        text = Localization.get("welcome_screen_title", currentLanguage),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        ),
                        color = PharaohGold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // App Subtitle
                    Text(
                        text = Localization.get("welcome_screen_subtitle", currentLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Introduction to Ancient Egyptian History Card (مقدمة قصيرة عن تاريخ مصر القديمة)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("welcome_history_intro_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
                    ),
                    border = BorderStroke(1.5.dp, PharaohGold.copy(alpha = 0.75f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Section Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(PharaohGold.copy(alpha = 0.2f))
                                    .border(1.dp, PharaohGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📜", fontSize = 22.sp)
                            }

                            Column {
                                Text(
                                    text = Localization.get("welcome_intro_title", currentLanguage),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "مهد الحضارة وفجر الضمير الإنساني" else "Cradle of Human Civilization",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                        }

                        // Paragraph 1: Nile and Foundation
                        Text(
                            text = Localization.get("welcome_intro_p1", currentLanguage),
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 23.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Justify
                        )

                        // Paragraph 2: Pyramids, Sciences, Hieroglyphs
                        Text(
                            text = Localization.get("welcome_intro_p2", currentLanguage),
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 23.sp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }

            // Key Historical Eras Highlights (3 Interactive/Informative Pillars)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "أعظم محطات التاريخ الفرعوني" else "Pillars of Pharaonic History",
                        style = MaterialTheme.typography.titleSmall,
                        color = PharaohGold,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    // Era 1: Pyramids
                    HistoryEraPill(
                        emoji = "🔺",
                        title = Localization.get("welcome_era1_title", currentLanguage),
                        desc = Localization.get("welcome_era1_desc", currentLanguage),
                        tag = if (currentLanguage == AppLanguage.ARABIC) "الدولة القديمة" else "Old Kingdom"
                    )

                    // Era 2: Empire
                    HistoryEraPill(
                        emoji = "⚔️",
                        title = Localization.get("welcome_era2_title", currentLanguage),
                        desc = Localization.get("welcome_era2_desc", currentLanguage),
                        tag = if (currentLanguage == AppLanguage.ARABIC) "الدولة الحديثة" else "New Kingdom"
                    )

                    // Era 3: Sciences & Hieroglyphs
                    HistoryEraPill(
                        emoji = "🔤",
                        title = Localization.get("welcome_era3_title", currentLanguage),
                        desc = Localization.get("welcome_era3_desc", currentLanguage),
                        tag = if (currentLanguage == AppLanguage.ARABIC) "العلوم والخلود" else "Science & Eternity"
                    )
                }
            }

            // App Features Section (ماذا ستكتشف في التطبيق؟)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = NileLapisDark.copy(alpha = 0.85f)
                    ),
                    border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = Localization.get("welcome_features_heading", currentLanguage),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )

                        FeatureBullet(
                            emoji = "🏛️",
                            text = Localization.get("welcome_feat_explore", currentLanguage)
                        )
                        FeatureBullet(
                            emoji = "📜",
                            text = Localization.get("welcome_feat_hieroglyphs", currentLanguage)
                        )
                        FeatureBullet(
                            emoji = "🎖️",
                            text = Localization.get("welcome_feat_quiz", currentLanguage)
                        )
                        FeatureBullet(
                            emoji = "🎧",
                            text = Localization.get("welcome_feat_audio", currentLanguage)
                        )
                    }
                }
            }

            // Primary Call-To-Action Button
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onStartJourney,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = PharaohGold)
                            .testTag("welcome_start_journey_button"),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PharaohGold,
                            contentColor = Color.Black
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = Localization.get("welcome_btn_start", currentLanguage),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Skip / Explore directly button
                    OutlinedButton(
                        onClick = onStartJourney,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .testTag("welcome_skip_button"),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PharaohGold
                        )
                    ) {
                        Text(
                            text = Localization.get("welcome_btn_skip", currentLanguage),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryEraPill(
    emoji: String,
    title: String,
    desc: String,
    tag: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(PharaohGold.copy(alpha = 0.15f))
                    .border(1.dp, PharaohGold.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 22.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PharaohGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall,
                            color = PharaohGold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FeatureBullet(
    emoji: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(emoji, fontSize = 18.sp)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
