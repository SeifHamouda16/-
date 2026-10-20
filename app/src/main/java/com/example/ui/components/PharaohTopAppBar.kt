package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharaohTopAppBar(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    onToggleTheme: (() -> Unit)? = null,
    onCycleThemeColor: (() -> Unit)? = null,
    isDarkTheme: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PharaohGold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                if (onNavigateBack != null) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "الرجوع",
                            tint = PharaohGold
                        )
                    }
                }
            },
            actions = {
                if (onCycleThemeColor != null) {
                    IconButton(
                        onClick = onCycleThemeColor,
                        modifier = Modifier.testTag("top_bar_colors_toggle")
                    ) {
                        Text(
                            text = "🎨",
                            fontSize = 18.sp
                        )
                    }
                }
                if (onToggleTheme != null) {
                    IconButton(
                        onClick = onToggleTheme,
                        modifier = Modifier.testTag("top_bar_theme_toggle")
                    ) {
                        Text(
                            text = if (isDarkTheme) "☀️" else "🌙",
                            fontSize = 18.sp
                        )
                    }
                }
                actions()
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        // Subtle gold bottom border line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, PharaohGoldDark, PharaohGold, Color.Transparent)
                    )
                )
        )
    }
}
