package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import com.example.model.RoyalThemeColor

@Composable
fun EgyptianCivilizationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    royalColor: RoyalThemeColor = CurrentRoyalThemeState.value,
    content: @Composable () -> Unit
) {
    // Keep CurrentRoyalThemeState synced so top-level PharaohGold tokens update instantly
    CurrentRoyalThemeState.value = royalColor

    val primaryColor = Color(royalColor.hexValue)
    val primaryLight = Color(royalColor.lightHexValue)
    val primaryDark = Color(royalColor.darkHexValue)
    val containerColor = Color(royalColor.containerHexValue)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = PureBlack,
            primaryContainer = containerColor,
            onPrimaryContainer = primaryLight,
            secondary = PureWhite,
            onSecondary = PureBlack,
            secondaryContainer = DarkSurfaceVariant,
            onSecondaryContainer = PureWhite,
            tertiary = TerracottaRed,
            onTertiary = PureWhite,
            background = PitchDarkBackground,
            onBackground = PureWhite,
            surface = DarkSurfaceCard,
            onSurface = PureWhite,
            surfaceVariant = DarkSurfaceVariant,
            onSurfaceVariant = TextMutedLight,
            outline = DarkOutline
        )
    } else {
        lightColorScheme(
            primary = primaryDark,
            onPrimary = PureWhite,
            primaryContainer = primaryLight.copy(alpha = 0.25f),
            onPrimaryContainer = PureBlack,
            secondary = PureBlack,
            onSecondary = PureWhite,
            secondaryContainer = LightSurfaceVariant,
            onSecondaryContainer = PureBlack,
            tertiary = TerracottaRed,
            onTertiary = PureWhite,
            background = BrightLightBackground,
            onBackground = TextDark,
            surface = LightSurfaceCard,
            onSurface = TextDark,
            surfaceVariant = LightSurfaceVariant,
            onSurfaceVariant = TextMuted,
            outline = LightOutline
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
