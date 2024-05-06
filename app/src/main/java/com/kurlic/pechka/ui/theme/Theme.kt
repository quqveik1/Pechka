package com.kurlic.pechka.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = BlueCupertino,
    secondary = GreenCupertino,
    tertiary = YellowCupertino,
    background = GreyCupertinoDark,
    surface = GreyCupertinoLight,
    onPrimary = WhiteCupertino,
    onSecondary = BlackCupertino,
    onTertiary = BlackCupertino,
    onBackground = WhiteCupertino,
    onSurface = WhiteCupertino,
    error = RedCupertino,
    onError = WhiteCupertino,
    onPrimaryContainer = BlueCupertinoDark
)

val LightColorScheme = lightColorScheme(
    primary = BlueCupertinoLight,
    secondary = GreenCupertinoLight,
    tertiary = YellowCupertinoLight,
    background = GreyCupertinoBackground,
    surface = GreyCupertinoSurface,
    onPrimary = WhiteCupertinoText,
    onSecondary = WhiteCupertinoText,
    onTertiary = BlackCupertinoText,
    onBackground = BlackCupertinoText,
    onSurface = BlackCupertinoText,
    error = RedCupertinoLight,
    onError = WhiteCupertinoText,
    onPrimaryContainer = GreenCupertinoLighter
)

@Composable fun PechkaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(
                window, view
            ).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}