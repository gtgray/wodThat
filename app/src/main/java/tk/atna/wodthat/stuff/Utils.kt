package tk.atna.wodthat.stuff

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import tk.atna.wodthat.R
import java.util.*


fun restartApplication(context: Context?) {
    context?.let {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.let {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            //
            Runtime.getRuntime().exit(0)
        }
    }
}

fun snackbar(view: View?, @StringRes messageRes: Int, length: Int) {
    snackbar(view, view?.resources?.getString(messageRes), length)
}

fun snackbar(view: View?, message: String?, length: Int) {
    view ?: return
    //
    val snackbar = Snackbar.make(view, message ?: "null", length)
        .setActionTextColor(getColor(view.context, R.color.clr_accent))
        .setAction("hide"/*R.string.hide*/) { /*just hide snack*/ }
    val tvMessage =
        snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
//        tvMessage.maxLines = 2
    snackbar.show()
}

fun getDimen(context: Context?, @DimenRes dimenRes: Int): Float {
    val resources = context?.resources
    resources ?: return 0f
    //
    return resources.getDimension(dimenRes) / resources.displayMetrics!!.density
}

fun dimenToPixels(context: Context?, @DimenRes dimenRes: Int): Int =
    context?.resources?.getDimensionPixelSize(dimenRes) ?: 0

fun dpToPixels(context: Context?, dp: Int): Int {
    return dpToPixels(context, dp.toFloat())
}

/** rounding-off as in {@link TypedValue#complexToDimensionPixelSize} */
fun dpToPixels(context: Context?, dp: Float): Int {
    context ?: return  0
    //
    val pixels = dpToPixelsFloat(context, dp)
    val res = when (pixels >= 0) {
        true -> (pixels + 0.5f).toInt()
        false -> (pixels - 0.5f).toInt()
    }
    //
    return when {
        (res != 0) -> res
        (dp > 0) -> 1
        else -> 0
    }
}

fun dpToPixelsFloat(context: Context?, dp: Float): Float {
    context ?: return 0f
    //
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

fun twoDigitsDouble(value: Double) = roundDouble(value, 2)

fun roundDouble(value: Double, digits: Int) =
    String.format(Locale.US, "%.0${digits}f", value).toFloat()

@ColorInt
fun getColor(context: Context?, @ColorRes colorRes: Int): Int {
    if (context != null
        && colorRes != 0) {
        return context.resources.getColor(colorRes, context.theme)
    }
    //
    return -1 // -1 is white color
}

fun getColorStateList(context: Context?, @ColorRes colorRes: Int): ColorStateList? {
    if (context != null
        && colorRes != 0) {
        return context.getColorStateList(colorRes)
    }
    //
    return null
}

fun localeToFlag(locale : Locale) = countryCodeToFlag(locale.country)

fun countryCodeToFlag(countryCode: String): String {
    // 0x41 is Letter A
    // 0x1F1E6 is Regional Indicator Symbol Letter A
    // Example :
    // firstLetter U => 20 + 0x1F1E6
    // secondLetter S => 18 + 0x1F1E6
    // See: https://en.wikipedia.org/wiki/Regional_Indicator_Symbol
    val firstLetter = Character.codePointAt(countryCode, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCode, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}

fun generateAvatar(context: Context, name: String, sizeDp: Int): Drawable {
    val textColor = getColor(context, R.color.clr_back)
    val backColor = getColor(context, R.color.clr_accent_55)
    val text = name.toUpperCase()[0].toString()
    val letterSize = dpToPixelsFloat(context, sizeDp * 0.5f) // letter fills % of image size
    val size = dpToPixels(context, sizeDp)
    //
    val newBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(newBitmap)
    canvas.drawColor(backColor)
//    val rect = canvas.clipBounds
    //
    val paint = Paint()
    paint.flags = Paint.ANTI_ALIAS_FLAG
    paint.color = textColor
    paint.textSize = letterSize
    val letterBounds = Rect()
    paint.getTextBounds(text, 0, 1, letterBounds)

//    Log.e("----------------- canvas bounds " + rect)
//    Log.e("----------------- letter spacing " + paint.getLetterSpacing())
//    Log.e("----------------- font spacing " + paint.getFontSpacing())
//    Log.e("----------------- letterBounds " + letterBounds)
//    Log.e("----------------- width " + letterBounds.width())
//    Log.e("----------------- height " + letterBounds.height())

    val x = (size - letterBounds.width().toFloat()) / 2 - letterBounds.left
    val y = (size + letterBounds.height().toFloat()) / 2

//    Log.e("----------------- x-y " + x + ", " + y)

    canvas.drawText(text, x, y, paint)
    //
    return BitmapDrawable(context.resources, newBitmap)
}
