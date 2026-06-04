package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MinGreenPrimaryDark,
    secondary = MinSecondaryDark,
    tertiary = MinGoldAccent,
    background = MinBgDark,
    surface = MinSurfaceDark,
    onPrimary = Color(0xFF08180E),
    onSecondary = Color(0xFF1A261C),
    onBackground = MinTextDark,
    onSurface = MinTextDark,
    surfaceVariant = MinHighlightContainerDark,
    onSurfaceVariant = MinMutedTextDark
)

private val LightColorScheme = lightColorScheme(
    primary = MinGreenPrimaryLight,
    secondary = MinSecondaryLight,
    tertiary = MinGoldAccent,
    background = MinBgLight,
    surface = MinSurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = MinTextLight,
    onSurface = MinTextLight,
    surfaceVariant = MinHighlightContainerLight,
    onSurfaceVariant = MinMutedTextLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Always enforce our precise custom spiritual minimal palette
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            var context = view.context
            var depth = 0
            while (context is android.content.ContextWrapper && depth < 20) {
                if (context is Activity) {
                    break
                }
                val base = (context as android.content.ContextWrapper).baseContext
                if (base == context || base == null) {
                    break
                }
                context = base
                depth++
            }
            val activity = context as? Activity
            if (activity != null) {
                val window = activity.window
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val windowInsetsController = WindowCompat.getInsetsController(window, view)
                windowInsetsController.isAppearanceLightStatusBars = !darkTheme
                windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
