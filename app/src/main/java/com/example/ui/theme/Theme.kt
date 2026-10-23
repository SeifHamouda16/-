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
    // Keep CurrentRoyalThemeState and CurrentIsDarkThemeState synced so top-level tokens update instantly
    CurrentIsDarkThemeState.value = darkTheme
    CurrentRoyalThemeState.value = royalColor

    val primaryColor = if (darkTheme) Color(royalColor.hexValue) else Color(royalColor.darkHexValue)
    val primaryLight = if (darkTheme) Color(royalColor.lightHexValue) else Color(royalColor.darkHexValue)
    val primaryDark = if (darkTheme) Color(0xFFC59B27) else Color(0xFF9E7817)
    val containerColor = if (darkTheme) Color(royalColor.containerHexValue) else Color(0xFFFFF3D6)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = PureBlack,
            primaryContainer = containerColor,
            onPrimaryContainer = PureWhite,
            secondary = primaryLight,
            onSecondary = PureBlack,
            secondaryContainer = DarkSurfaceVariant,
            onSecondaryContainer = PureWhite,
            tertiary = primaryDark,
            onTertiary = PureBlack,
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
            primary = primaryColor,
            onPrimary = PureWhite,
            primaryContainer = containerColor,
            onPrimaryContainer = PureBlack,
            secondary = primaryDark,
            onSecondary = PureWhite,
            secondaryContainer = LightSurfaceVariant,
            onSecondaryContainer = PureBlack,
            tertiary = primaryColor,
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
