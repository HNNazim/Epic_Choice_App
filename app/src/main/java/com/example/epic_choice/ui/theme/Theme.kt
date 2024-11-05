package com.example.epic_choice.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFE60D0D), // Red
    secondary = Color(0xFF0DDFE6), // Cyan
    tertiary = Color(0xFFED6016), // Orange
    background = Color(0xFFFFFFFF), // White background
    surface = Color(0xFFFFFFFF), // White surface
    onPrimary = Color.White, // Text color on primary
    onSecondary = Color.Black, // Text color on secondary
    onTertiary = Color.Black, // Text color on tertiary
    onBackground = Color.Black, // Text color on background
    onSurface = Color.Black // Text color on surface
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFE60D0D), // Red
    secondary = Color(0xFF0DDFE6), // Cyan
    tertiary = Color(0xFFED6016), // Orange
    background = Color(0xFF121212), // Dark background
    surface = Color(0xFF121212), // Dark surface
    onPrimary = Color.Black, // Text color on primary
    onSecondary = Color.Black, // Text color on secondary
    onTertiary = Color.Black, // Text color on tertiary
    onBackground = Color.White, // Text color on background
    onSurface = Color.White // Text color on surface
)

@Composable
fun EpicChoiceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Automatically uses dark mode if system is in dark theme
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
