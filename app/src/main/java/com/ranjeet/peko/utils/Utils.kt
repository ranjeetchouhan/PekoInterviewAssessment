package com.ranjeet.peko.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {


    fun convertTimestampToReadable(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy, HH:mm:ss a", Locale.getDefault())

        return try {
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date)
        } catch (e: Exception) {
            "Invalid Timestamp"
        }
    }

    fun convertTimestampToReadableDateOnly(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

        return try {
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date)
        } catch (e: Exception) {
            "Invalid Timestamp"
        }
    }
}