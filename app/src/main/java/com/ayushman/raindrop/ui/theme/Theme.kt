package com.ayushman.raindrop.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val LightColorPalette = lightColors(
    primary = PrimaryBlack,
    primaryVariant = PrimaryBlack,
    secondary = BackgroundWhite,
    surface = BackgroundWhite,
    background = BackgroundWhite,
    onPrimary = White,
    onSecondary = Black,
    onSurface = Black,
    onBackground = Black,
)

@Composable
fun RaindropTheme(content: @Composable () -> Unit) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}