package com.example.data.settings

import android.content.Context
import android.content.SharedPreferences

class UserSettings(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("friendly_eats_user_settings", Context.MODE_PRIVATE)

    var geminiApiKey: String
        get() = prefs.getString("gemini_api_key", "") ?: ""
        set(value) = prefs.edit().putString("gemini_api_key", value).apply()

    var userName: String
        get() = prefs.getString("user_name", "Foodie Guest") ?: "Foodie Guest"
        set(value) = prefs.edit().putString("user_name", value).apply()

    var userEmail: String
        get() = prefs.getString("user_email", "Skiza89@gmail.com") ?: "Skiza89@gmail.com"
        set(value) = prefs.edit().putString("user_email", value).apply()

    // Flag for initial database load to verify one-click setups
    var hasSeeded: Boolean
        get() = prefs.getBoolean("has_seeded_v1", false)
        set(value) = prefs.edit().putBoolean("has_seeded_v1", value).apply()
}
