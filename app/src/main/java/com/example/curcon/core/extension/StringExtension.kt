package com.example.curcon.core.extension

import okhttp3.internal.toLongOrDefault

fun String.formatCurrency(): String {
    return try {
        // First remove all commas and non-numeric characters (except decimal point)
        val cleanNumber = this
            .replace(",", "")  // Remove all existing commas // Remove all existing commas
            .replace(Regex("[^0-9.]"), "")  // Remove any other non-numeric characters except decimal
            .toDouble()
        // Format with proper comma placement
        String.format("%,.2f", cleanNumber)  // This will add commas every 3 digits
    } catch (e: Exception) {
        this
    }
}

