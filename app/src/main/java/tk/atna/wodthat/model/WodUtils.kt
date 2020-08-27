package tk.atna.wodthat.model

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import tk.atna.wodthat.R
import tk.atna.wodthat.internal.backgroundTint
import tk.atna.wodthat.internal.textSize
import tk.atna.wodthat.stuff.generateAvatar
import tk.atna.wodthat.stuff.getColor
import tk.atna.wodthat.stuff.roundDouble


fun formatHeadline(headline: String): String {
    val pattern = "as many rounds as possible "
    return applyPattern(headline, pattern, pattern.length)
}

private fun applyPattern(source: String, pattern: String, offset: Int): String {
    val found = source.indexOf(pattern, 0)
    if (found != -1) {
        val index = found + offset
        return makeTwoLines(source, index)
    }
    //
    return source
}

private fun makeTwoLines(source: String, index: Int): String {
    if(index in 0..source.length)
        return StringBuilder()
            .append(source.substring(0, index).trim())
            .appendln()
            .append(source.substring(index, source.length).trim())
            .toString()
    //
    return source
}

fun addTextLine(parent: ViewGroup, text: CharSequence, @DimenRes textSizeRes: Int) {
    val line = TextView(parent.context)
    line.setTextColor(getColor(parent.context, R.color.clr_black_55))
    line.textSize(textSizeRes)
    line.text = text
    parent.addView(
        line,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

fun addWodExercises(parent: ViewGroup, exercises: List<WodExercise>, @DimenRes textSizeRes: Int) {
    exercises.forEach { item ->
        val text = buildText(
            " ",
            item.repsScheme ?: "",
            addColoredSpan(
                item.name,
                getColor(parent.context, R.color.clr_black_85)
            ),
            addWeight(item.manWeight, item.womanWeight, item.units)
        )
        //
        addTextLine(parent, text, textSizeRes)
    }
}

fun buildText(separator: String, vararg chunks: CharSequence): CharSequence {
    val text = SpannableStringBuilder()
    chunks.forEachIndexed { index, chunk ->
        text.append(chunk)
        if (chunk.isNotEmpty() && index < chunks.size - 1)
            text.append(separator)
    }
    //
    return text
}

fun addColoredSpan(text: String, @ColorInt color: Int): Spannable {
    val spannable = SpannableStringBuilder(text)
    spannable.setSpan(
        ForegroundColorSpan(color),
        0,
        text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}

private fun addWeight(man: String?, woman: String?, units: String?): String {
    return StringBuilder(if (man != null || woman != null) "- " else "")
        .append(man ?: "")
        .append(if (man != null && woman != null) "/" else "")
        .append(woman ?: "")
        .append(if (units?.contains("%") == true) "" else " ")
        .append(units ?: "")
        .toString()
}

fun lbToKg(lb: Float): Float {
    return roundDouble(lb * 0.45359237, 1)
}

fun discoverHtmlWodDesc(wod: WodDetailed): CharSequence {
    val chunks = arrayOf<CharSequence>(
        wod.description.replace("\n", "<br/>")/*.also { Log.w("------------ DESC ${wod.description}") }*/,
        *(wod.comments
            .filter { comment -> comment.author == wod.author }
            .map { comment -> comment.text }
            .map { text -> text.replace("\n", "<br/>") }
            .toTypedArray())
    )
    //
    return buildText("\n", *chunks)
}

fun placeRank(textView: TextView, rank: Int) {
    when (rank) {
        0 -> return // do nothing
        1 -> { textView.backgroundTint(R.color.clr_gold) }
        2 -> { textView.backgroundTint(R.color.clr_silver) }
        3 -> { textView.backgroundTint(R.color.clr_bronze) }
        else -> { textView.backgroundTint(R.color.clr_back) }
    }
    //
    textView.text = "$rank"
}

fun placeGender(imageView: ImageView, gender: Int) {
    when(gender) {
        0 -> { imageView.setImageResource(R.drawable.svg_male) }
        1 -> { imageView.setImageResource(R.drawable.svg_female) }
        else -> return
    }
}

fun discoverReps(source: WodResult): String {
    return if(source.weight > 0)
        "${lbToKg(source.weight)} kg"
    else if (source.timeLimit > 0
        && source.reps > 0) {
        val fullRounds = source.reps / source.repsPerRound
        val tail = source.reps % source.repsPerRound
        "${source.reps}($fullRounds+$tail)"
    // simple reps count
    } else
        source.reps.toString()
}

fun genderToEmoji(gender: Int?): CharSequence? {
    return when(gender) {
        0 -> "\u2642"
        1 -> "\u2640"
        else -> null
    }
}

fun placeGeneratedAvatar(imageView: ImageView, name: String, sizeDp: Int) {
    imageView.setImageDrawable(generateAvatar(imageView.context, name, sizeDp))
}

