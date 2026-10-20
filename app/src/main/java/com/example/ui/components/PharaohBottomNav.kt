package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Localization
import com.example.model.AppLanguage
import com.example.model.ScreenType
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark

@Composable
fun PharaohBottomNav(
    currentScreen: ScreenType,
    currentLanguage: AppLanguage,
    onNavigate: (ScreenType) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, PharaohGoldDark, PharaohGold, Color.Transparent)
                    )
                )
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .testTag("pharaoh_bottom_nav"),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            NavigationBarItem(
                selected = currentScreen == ScreenType.HOME || currentScreen == ScreenType.SECTION_DETAIL,
                onClick = { onNavigate(ScreenType.HOME) },
                icon = { Icon(Icons.Default.AutoStories, contentDescription = Localization.get("nav_sections", currentLanguage)) },
                label = { Text(Localization.get("nav_sections", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = PharaohGold,
                    indicatorColor = PharaohGold,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_home")
            )

            NavigationBarItem(
                selected = currentScreen == ScreenType.HIEROGLYPHS,
                onClick = { onNavigate(ScreenType.HIEROGLYPHS) },
                icon = { Icon(Icons.Default.Translate, contentDescription = Localization.get("nav_hieroglyphs", currentLanguage)) },
                label = { Text(Localization.get("nav_hieroglyphs", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = PharaohGold,
                    indicatorColor = PharaohGold,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_hieroglyphs")
            )

            NavigationBarItem(
                selected = currentScreen == ScreenType.ACHIEVEMENTS,
                onClick = { onNavigate(ScreenType.ACHIEVEMENTS) },
                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = Localization.get("nav_badges", currentLanguage)) },
                label = { Text(Localization.get("nav_badges", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = PharaohGold,
                    indicatorColor = PharaohGold,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_achievements")
            )

            NavigationBarItem(
                selected = currentScreen == ScreenType.ACCOUNT_SETTINGS,
                onClick = { onNavigate(ScreenType.ACCOUNT_SETTINGS) },
                icon = { Icon(Icons.Default.Person, contentDescription = Localization.get("nav_account", currentLanguage)) },
                label = { Text(Localization.get("nav_account", currentLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = PharaohGold,
                    indicatorColor = PharaohGold,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_account")
            )

        }
    }
}
