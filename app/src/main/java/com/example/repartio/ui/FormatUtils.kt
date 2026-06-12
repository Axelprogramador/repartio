package com.example.repartio.ui

import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(amount)
    }
}