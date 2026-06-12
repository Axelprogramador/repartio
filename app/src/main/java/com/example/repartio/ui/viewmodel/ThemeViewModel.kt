package com.example.repartio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repartio.ui.theme.AppLanguage
import com.example.repartio.ui.theme.AppTheme
import com.example.repartio.ui.theme.DarkModePreference
import com.example.repartio.ui.theme.LanguageManager
import com.example.repartio.ui.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeManager: ThemeManager,
    private val languageManager: LanguageManager
) : ViewModel() {

    val currentTheme = themeManager.currentTheme
    val darkModePreference = themeManager.darkModePreference

    private val _currentLanguage = MutableStateFlow(languageManager.getCurrentLanguage())
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage

    val isLanguageChangeSupported = languageManager.isSupported

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeManager.setTheme(theme)
        }
    }

    fun setDarkMode(preference: DarkModePreference) {
        viewModelScope.launch {
            themeManager.setDarkMode(preference)
        }
    }

    fun setLanguage(language: AppLanguage) {
        languageManager.setLanguage(language)
        _currentLanguage.value = language
    }
}