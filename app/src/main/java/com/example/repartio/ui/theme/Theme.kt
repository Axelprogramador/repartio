package com.example.repartio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.repartio.ui.viewmodel.ThemeViewModel

private val OceanLightColors = lightColorScheme(
    primary = OceanPrimary,
    secondary = OceanSecondary,
    tertiary = OceanTertiary,
    background = OceanBackground,
    surface = OceanSurface
)

private val OceanDarkColors = darkColorScheme(
    primary = OceanPrimaryDark,
    secondary = OceanSecondaryDark,
    background = OceanBackgroundDark,
    surface = OceanSurfaceDark
)

private val ForestLightColors = lightColorScheme(
    primary = ForestPrimary,
    secondary = ForestSecondary,
    tertiary = ForestTertiary,
    background = ForestBackground,
    surface = ForestSurface
)

private val ForestDarkColors = darkColorScheme(
    primary = ForestPrimaryDark,
    secondary = ForestSecondaryDark,
    background = ForestBackgroundDark,
    surface = ForestSurfaceDark
)

private val SunsetLightColors = lightColorScheme(
    primary = SunsetPrimary,
    secondary = SunsetSecondary,
    tertiary = SunsetTertiary,
    background = SunsetBackground,
    surface = SunsetSurface
)

private val SunsetDarkColors = darkColorScheme(
    primary = SunsetPrimaryDark,
    secondary = SunsetSecondaryDark,
    background = SunsetBackgroundDark,
    surface = SunsetSurfaceDark
)

@Composable
fun RepartioTheme(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val currentTheme by themeViewModel.currentTheme.collectAsState(initial = AppTheme.OCEAN)
    val darkModePreference by themeViewModel.darkModePreference.collectAsState(initial = DarkModePreference.SYSTEM)
    val systemDarkTheme = isSystemInDarkTheme()

    // Determina modo segun  preferencia del usuario
    val darkTheme = when (darkModePreference) {
        DarkModePreference.SYSTEM -> systemDarkTheme
        DarkModePreference.LIGHT -> false
        DarkModePreference.DARK -> true
    }

    val colorScheme = when (currentTheme) {
        AppTheme.OCEAN -> if (darkTheme) OceanDarkColors else OceanLightColors
        AppTheme.FOREST -> if (darkTheme) ForestDarkColors else ForestLightColors
        AppTheme.SUNSET -> if (darkTheme) SunsetDarkColors else SunsetLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}