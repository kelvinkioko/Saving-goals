package com.savings.savinggoals.ui.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.savings.savinggoals.R

class SettingsViewModel : ViewModel() {
    fun shareApp(activity: Activity) {
        val googlePlayUrl = "https://play.google.com/store/apps/details?id="
        val msg = activity.getString(R.string.share_message) + " "

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, msg + googlePlayUrl + activity.packageName)
        shareIntent.type = "text/plain"
        activity.startActivity(Intent.createChooser(shareIntent, "Share..."))
    }

    fun rateApp(activity: Activity) {
        activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${activity.packageName}")))
    }
}
