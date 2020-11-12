package com.savings.savinggoals.constants

import android.content.Context
import android.content.SharedPreferences

class PreferenceHandler(activity: Context) {

    private val sharedPrefFile = "WealthyPreference"

    private val sharedPreferences: SharedPreferences = activity.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    fun setFirstTimeOpen(firstTimeOpen: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("firstTimeOpen", firstTimeOpen)
        editor.apply()
    }

    fun firstTimeOpen(): Boolean? {
        return sharedPreferences.getBoolean("firstTimeOpen", true)
    }

    fun setInterstitialCountDown(isInterstitialCountDown: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("isInterstitialCountDown", isInterstitialCountDown)
        editor.apply()
    }

    fun getInterstitialCount(): Int {
        return sharedPreferences.getInt("isInterstitialCountDown", 0)
    }

    fun setCurrency(currencySelection: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("currency", currencySelection)
        editor.apply()
    }

    fun getCurrency(): String? {
        return sharedPreferences.getString("currency", "USD")
    }
}
