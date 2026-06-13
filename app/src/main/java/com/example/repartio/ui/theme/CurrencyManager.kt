package com.example.repartio.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class CurrencyPreference(val code: String, val symbol: String) {
    AUTO("", ""),
    USD("USD", "$"),
    EUR("EUR", "€"),
    GBP("GBP", "£"),
    JPY("", "¥"),
    BRL("BRL", "R$"),
    MXN("MXN", "MX$")
}

@Singleton
class CurrencyManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val CURRENCY_KEY = stringPreferencesKey("currency")

    val currencyPreference: Flow<CurrencyPreference> = context.dataStore.data.map { preferences ->
        val value = preferences[CURRENCY_KEY] ?: CurrencyPreference.AUTO.name
        CurrencyPreference.valueOf(value)
    }

    suspend fun setCurrency(preference: CurrencyPreference) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY_KEY] = preference.name
        }
    }
}