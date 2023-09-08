package com.samthedev.mochimix.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),

    primary = tonalPalette.primary80,
        onPrimary = tonalPalette.primary20,
        primaryContainer = tonalPalette.primary30,
        onPrimaryContainer = tonalPalette.primary90,
        inversePrimary = tonalPalette.primary40,
        secondary = tonalPalette.secondary80,
        onSecondary = tonalPalette.secondary20,
        secondaryContainer = tonalPalette.secondary30,
        onSecondaryContainer = tonalPalette.secondary90,
        tertiary = tonalPalette.tertiary80,
        onTertiary = tonalPalette.tertiary20,
        tertiaryContainer = tonalPalette.tertiary30,
        onTertiaryContainer = tonalPalette.tertiary90,
        background = tonalPalette.neutral10,
        onBackground = tonalPalette.neutral90,
        surface = tonalPalette.neutral10,
        onSurface = tonalPalette.neutral90,
        surfaceVariant = tonalPalette.neutralVariant30,
        onSurfaceVariant = tonalPalette.neutralVariant80,
        inverseSurface = tonalPalette.neutral90,
        inverseOnSurface = tonalPalette.neutral20,
        outline = tonalPalette.neutralVariant60,
    */
)

@Composable
fun MochiMixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}