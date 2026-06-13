package com.example.repartio.ui

import com.example.repartio.ui.theme.CurrencyPreference
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object FormatUtils {
    fun formatCurrency(amount: Double, preference: CurrencyPreference = CurrencyPreference.AUTO): String {
        return if (preference == CurrencyPreference.AUTO) {
            NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)
        } else {
            val format = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance(preference.code)
            format.format(amount)
        }
    }
}