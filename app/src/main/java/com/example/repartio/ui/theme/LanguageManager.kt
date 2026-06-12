package com.example.repartio.ui.theme

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class AppLanguage(val tag: String, val displayName: String) {
    SYSTEM("", "System"),
    ENGLISH("en", "English"),
    SPANISH("es", "Español")
}

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Solo disponible en Android 13+
    fun setLanguage(language: AppLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            if (language == AppLanguage.SYSTEM) {
                localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
            } else {
                localeManager.applicationLocales = LocaleList.forLanguageTags(language.tag)
            }
        }
    }

    fun getCurrentLanguage(): AppLanguage {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            val tag = localeManager.applicationLocales[0]?.language ?: ""
            return AppLanguage.entries.find { it.tag == tag } ?: AppLanguage.SYSTEM
        }
        return AppLanguage.SYSTEM
    }

    // True si el dispositivo soporta cambio de idioma por app, Android 13 para abajo no sorpota
    val isSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}