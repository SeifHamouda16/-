package com.example.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.model.RoyalThemeColor

// Pure Monochrome Black & White Palette
val PureBlack = Color(0xFF000000)
val PitchDarkBackground = Color(0xFF080808)
val DarkSurfaceCard = Color(0xFF141414)
val DarkSurfaceVariant = Color(0xFF222222)
val DarkOutline = Color(0xFF333333)

val PureWhite = Color(0xFFFFFFFF)
val BrightLightBackground = Color(0xFFFFFFFF)
val LightSurfaceCard = Color(0xFFF8F8F8)
val LightSurfaceVariant = Color(0xFFEFEFEF)
val LightOutline = Color(0xFFD8D8D8)

// Active Royal Accent Theme State
val CurrentRoyalThemeState = mutableStateOf(RoyalThemeColor.GOLD)

// Dynamic Royal Pharaoh Tokens that adapt immediately to the user's selected color
val PharaohGold: Color
    get() = Color(CurrentRoyalThemeState.value.hexValue)

val PharaohGoldLight: Color
    get() = Color(CurrentRoyalThemeState.value.lightHexValue)

val PharaohGoldDark: Color
    get() = Color(CurrentRoyalThemeState.value.darkHexValue)

val PharaohGoldContainer: Color
    get() = Color(CurrentRoyalThemeState.value.containerHexValue)

// Secondary & Semantic Tokens
val NileLapisDark = Color(0xFF121212)
val NileLapisNavy = Color(0xFF1C1C1C)
val NileLapisLight = Color(0xFF2A2A2A)

val TerracottaRed = Color(0xFFD32F2F)
val MalachiteGreen = Color(0xFF2E7D32)

val TextLight = Color(0xFFFFFFFF)
val TextDark = Color(0xFF0D0D0D)
val TextMuted = Color(0xFF757575)
val TextMutedLight = Color(0xFFAAAAAA)
