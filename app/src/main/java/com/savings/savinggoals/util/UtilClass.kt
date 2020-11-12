package com.savings.savinggoals.util

import android.content.Context
import androidx.room.TypeConverter
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale

fun setupBannerAdView(context: Context): AdView {
    return AdView(context, "2623680264372670_4444174165656595", AdSize.BANNER_HEIGHT_50)
}

fun setupInterstitialAdView(context: Context): InterstitialAd {
    return InterstitialAd(context, "2623680264372670_4445452982195380")
}

fun setupInterstitialListener(interstitialAd: InterstitialAd): InterstitialAdListener {
    return object : InterstitialAdListener {
        override fun onInterstitialDisplayed(ad: Ad) {
            // Interstitial ad displayed callback
            println("Interstitial ad displayed.")
        }

        override fun onInterstitialDismissed(ad: Ad) {
            // Interstitial dismissed callback
            println("Interstitial ad dismissed.")
        }

        override fun onError(ad: Ad, adError: AdError) {
            // Ad error callback
            println("Interstitial ad failed to load: " + adError.errorMessage)
        }

        override fun onAdLoaded(ad: Ad) {
            // Interstitial ad is loaded and ready to be displayed
            println("Interstitial ad is loaded and ready to be displayed!")
            // Show the ad
            // interstitialAd.show()
        }

        override fun onAdClicked(ad: Ad) {
            // Ad clicked callback
            println("Interstitial ad clicked!")
        }

        override fun onLoggingImpression(ad: Ad) {
            // Ad impression logged callback
            println("Interstitial ad impression logged!")
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

fun TextInputEditText.getUnformatedAmount(): String {
    return text.toString().replace("[,]".toRegex(), "")
}

fun TextInputEditText.formatAmount(stringText: String, currency: String): String {
    val secondCleanString = UtilsClass.amountFormatter(cleanAmount(stringText))
    setText("$secondCleanString $currency")
    setSelection(secondCleanString.length)
    return secondCleanString
}

private fun cleanAmount(dirtyAmount: String): String {
    return dirtyAmount.split(" ")[0]
}

fun toTitleCase(str: String?, handleWhiteSpace: Boolean): String? {
    if (str == null) {
        return null
    }
    var space = true
    val builder = StringBuilder(str)
    val len = builder.length
    for (i in 0 until len) {
        val c = builder[i]
        if (space) {
            if (!Character.isWhitespace(c)) {
                // Convert to title case and switch out of whitespace mode.
                builder.setCharAt(i, Character.toTitleCase(c))
                space = false
            }
        } else if (Character.isWhitespace(c)) {
            space = handleWhiteSpace
        } else {
            builder.setCharAt(i, Character.toLowerCase(c))
        }
    }
    return builder.toString()
}

fun Float.formatAmount(): String {
    val pattern = "###,##0.00"
    val decFormat = DecimalFormat(pattern)
    return decFormat.format(this)
}

fun String.formatAmount(): String {
    val pattern = "###,##0.00"
    val decFormat = DecimalFormat(pattern)
    if (this.isNullOrEmpty()) return "0.00"
    return decFormat.format(this.toDouble())
}

fun String.dateFormat(): String {
    val d = SimpleDateFormat("ddMMYYYY", Locale.getDefault()).parse(this)
    return SimpleDateFormat("dd MMM, YYYY", Locale.getDefault()).format(d)
}

private class UtilsClass {

    companion object {
        fun amountFormatter(amount: String): String {
            val locale: Locale = Locale.UK
            val currency = Currency.getInstance(locale)
            val cleanString = amount.replace("[,.]".toRegex(), "")
            val parsed = cleanString.toDouble()
            val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100)
            return formatted.replace("[${currency.symbol}]".toRegex(), "")
        }
    }
}
