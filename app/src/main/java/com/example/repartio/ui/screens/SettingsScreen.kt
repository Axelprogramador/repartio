package com.example.repartio.ui.screens

import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.repartio.R
import com.example.repartio.ui.theme.AppLanguage
import com.example.repartio.ui.theme.AppTheme
import com.example.repartio.ui.theme.CurrencyPreference
import com.example.repartio.ui.theme.DarkModePreference
import com.example.repartio.ui.theme.ForestPrimary
import com.example.repartio.ui.theme.ForestSecondary
import com.example.repartio.ui.theme.OceanPrimary
import com.example.repartio.ui.theme.OceanSecondary
import com.example.repartio.ui.theme.SunsetPrimary
import com.example.repartio.ui.theme.SunsetSecondary
import com.example.repartio.ui.viewmodel.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val currentTheme by themeViewModel.currentTheme.collectAsState(initial = AppTheme.OCEAN)
    val darkModePreference by themeViewModel.darkModePreference.collectAsState(initial = DarkModePreference.SYSTEM)
    val currentLanguage by themeViewModel.currentLanguage.collectAsState()
    val currentCurrency by themeViewModel.currencyPreference.collectAsState(initial = CurrencyPreference.AUTO)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Sección apariencia
            SettingsSection(title = stringResource(R.string.appearance)) {
                Text(
                    stringResource(R.string.theme),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppTheme.entries.forEach { theme ->
                        ThemeCard(
                            theme = theme,
                            isSelected = currentTheme == theme,
                            onClick = { themeViewModel.setTheme(theme) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    stringResource(R.string.dark_mode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DarkModePreference.entries.forEach { preference ->
                        FilterChip(
                            selected = darkModePreference == preference,
                            onClick = { themeViewModel.setDarkMode(preference) },
                            label = {
                                Text(
                                    when (preference) {
                                        DarkModePreference.SYSTEM -> stringResource(R.string.dark_mode_system)
                                        DarkModePreference.LIGHT -> stringResource(R.string.dark_mode_light)
                                        DarkModePreference.DARK -> stringResource(R.string.dark_mode_dark)
                                    }
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Sección idioma — solo visible en Android 13+
            if (themeViewModel.isLanguageChangeSupported) {
                SettingsSection(title = stringResource(R.string.language)) {
                    Text(
                        stringResource(R.string.language_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppLanguage.entries.forEach { language ->
                            FilterChip(
                                selected = currentLanguage == language,
                                onClick = { themeViewModel.setLanguage(language) },
                                label = {
                                    Text(
                                        when (language) {
                                            AppLanguage.SYSTEM -> stringResource(R.string.dark_mode_system)
                                            AppLanguage.ENGLISH -> "English"
                                            AppLanguage.SPANISH -> "Español"
                                            AppLanguage.FRENCH -> "Français"
                                            AppLanguage.GERMAN -> "Deutsch"
                                            AppLanguage.PORTUGUESE -> "Português"
                                            AppLanguage.CHINESE -> "中文"
                                            AppLanguage.JAPANESE -> "日本語"
                                            AppLanguage.KOREAN -> "한국어"
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }


            // Sección moneda
            SettingsSection(title = stringResource(R.string.currency)) {
                Text(
                    stringResource(R.string.currency_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CurrencyPreference.entries.forEach { currency ->
                        FilterChip(
                            selected = currentCurrency == currency,
                            onClick = { themeViewModel.setCurrency(currency) },
                            label = {
                                Text(
                                    if (currency == CurrencyPreference.AUTO)
                                        stringResource(R.string.currency_auto)
                                    else
                                        "${currency.symbol} ${currency.code}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (name, primary, secondary) = when (theme) {
        AppTheme.OCEAN -> Triple(stringResource(R.string.theme_ocean), OceanPrimary, OceanSecondary)
        AppTheme.FOREST -> Triple(stringResource(R.string.theme_forest), ForestPrimary, ForestSecondary)
        AppTheme.SUNSET -> Triple(stringResource(R.string.theme_sunset), SunsetPrimary, SunsetSecondary)
    }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                ) else Modifier
            ),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Surface(
                    color = primary,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {}
                Surface(
                    color = secondary,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {}
            }
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}