package com.savings.savinggoals.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

/**
 * Get pictures through uri and compress them
 *
 * @param uri
 */
@Throws(FileNotFoundException::class, IOException::class)
fun getBitmapFormUri(activity: Activity, uri: Uri?): String {
    var input: InputStream? = activity.contentResolver.openInputStream(uri!!)
    val onlyBoundsOptions = BitmapFactory.Options()
    onlyBoundsOptions.inJustDecodeBounds = true
    onlyBoundsOptions.inDither = true // optional
    onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 // optional
    BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
    input!!.close()
    val originalWidth = onlyBoundsOptions.outWidth
    val originalHeight = onlyBoundsOptions.outHeight
    if (originalWidth == -1 || originalHeight == -1) return ""
    // Image resolution is based on 480x800
    val hh = 800f // The height is set as 800f here
    val ww = 480f // Set the width here to 480f
    // Zoom ratio. Because it is a fixed scale, only one data of height or width is used for calculation
    var be = 1 // be=1 means no scaling
    if (originalWidth > originalHeight && originalWidth > ww) { // If the width is large, scale according to the fixed size of the width
        be = (originalWidth / ww).toInt()
    } else if (originalWidth < originalHeight && originalHeight > hh) { // If the height is high, scale according to the fixed size of the width
        be = (originalHeight / hh).toInt()
    }
    if (be <= 0) be = 1
    // Proportional compression
    val bitmapOptions = BitmapFactory.Options()
    bitmapOptions.inSampleSize = be // Set scaling
    bitmapOptions.inDither = true // optional
    bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 // optional
    input = activity.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
    input!!.close()
    return compressImage(bitmap!!) // Mass compression again
}

/**
 * Mass compression method
 *
 * @param image
 * @return
 */
fun compressImage(image: Bitmap): String {
    val baos = ByteArrayOutputStream()
    image.compress(
        Bitmap.CompressFormat.JPEG,
        100,
        baos
    ) // Quality compression method, here 100 means no compression, store the compressed data in the BIOS
    var options = 100
    while (baos.toByteArray().size / 1024 > 100) { // Cycle to determine if the compressed image is greater than 100kb, greater than continue compression
        baos.reset() // Reset the BIOS to clear it
        // First parameter: picture format, second parameter: picture quality, 100 is the highest, 0 is the worst, third parameter: save the compressed data stream
        image.compress(
            Bitmap.CompressFormat.JPEG,
            options,
            baos
        ) // Here, the compression options are used to store the compressed data in the BIOS
        options -= 10 // 10 less each time
    }
    val isBm = ByteArrayInputStream(baos.toByteArray()) // Store the compressed data in ByteArrayInputStream
    return BitmapFactory.decodeStream(isBm, null, null)?.let { bitMapToString(it) }!!
}

fun bitMapToString(bitmap: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun stringToBitMap(base64String: String): Bitmap {
    val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}
