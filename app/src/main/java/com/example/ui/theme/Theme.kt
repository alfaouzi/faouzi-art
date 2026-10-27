package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FaouziColorScheme = darkColorScheme(
    primary = FaouziLightTurquoise,
    onPrimary = FaouziDarkTeal,
    primaryContainer = FaouziMediumTeal,
    onPrimaryContainer = Color.White,
    secondary = FaouziGoldWarm,
    onSecondary = FaouziDarkTeal,
    background = FaouziDarkTeal,
    onBackground = TextPrimary,
    surface = FaouziDeepTeal,
    onSurface = TextPrimary,
    surfaceVariant = FaouziMediumTeal,
    onSurfaceVariant = TextSecondary,
    outline = FaouziSurfaceGlassBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep signature turquoise & teal brand identity consistent
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FaouziColorScheme,
        typography = Typography,
        content = content
    )
}
