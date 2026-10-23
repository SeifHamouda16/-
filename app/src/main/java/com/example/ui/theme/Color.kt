package com.example.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.model.RoyalThemeColor

// Pure Colors & Backgrounds
val PureBlack = Color(0xFF000000)
val PitchDarkBackground = Color(0xFF0A1118)  // Majestic deep lapis night
val DarkSurfaceCard = Color(0xFF0F1D2C)      // Royal lapis card surface
val DarkSurfaceVariant = Color(0xFF162536)   // Lapis surface variant
val DarkOutline = Color(0xFF2A3D52)          // Lapis outline border

val PureWhite = Color(0xFFFFFFFF)
val BrightLightBackground = Color(0xFFFDFBF7) // Warm papyrus & alabaster light background
val LightSurfaceCard = Color(0xFFFFFFFF)      // Pure white crisp card
val LightSurfaceVariant = Color(0xFFF4EFE6)   // Warm papyrus limestone variant
val LightOutline = Color(0xFFE2D7C3)          // Warm golden outline

// Active Theme State
val CurrentIsDarkThemeState = mutableStateOf(true)
val CurrentRoyalThemeState = mutableStateOf(RoyalThemeColor.GOLD)

// Royal Pharaoh Gold Tokens
val PharaohGold: Color
    get() = if (CurrentIsDarkThemeState.value) Color(0xFFE5C06E) else Color(0xFFC59B27)

val PharaohGoldLight: Color
    get() = if (CurrentIsDarkThemeState.value) Color(0xFFF3DE9C) else Color(0xFFB8860B)

val PharaohGoldDark: Color
    get() = Color(0xFF9E7817)

val PharaohGoldContainer: Color
    get() = if (CurrentIsDarkThemeState.value) Color(0xFF2C2208) else Color(0xFFFFF3D6)

// Pharaonic Royal Palette (Nile Lapis, Sinai Turquoise, Terracotta, Papyrus)
val NileLapisDark = Color(0xFF0A1118)
val NileLapisNavy = Color(0xFF0F1D2C)
val NileLapisLight = Color(0xFF1E354D)

val TerracottaRed = Color(0xFFC0392B)
val MalachiteGreen = Color(0xFF27AE60)

val TextLight = Color(0xFFFFFFFF)
val TextDark = Color(0xFF1A1A1A)
val TextMuted = Color(0xFF888888)
val TextMutedLight = Color(0xFFB0BEC5)
