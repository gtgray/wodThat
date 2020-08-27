package tk.atna.wodthat.stuff

import android.text.format.DateFormat
import java.util.*

object TimestampUtils {

    private const val FORMAT_DATE_FULL = "dd.MM.yyyy"
    private const val FORMAT_DATE_SHORT = "dd MMM"
    private const val FORMAT_DURATION = "mm:ss"
    private const val FORMAT_LONG_DURATION = "kk:mm:ss"


    fun secsToLocalDate(secs : Int) = millisToLocalDate(secs.toLong() * 1000)

    fun millisToLocalDate(millis: Long) = millisToLocalDate(millis, FORMAT_DATE_FULL)

    private fun millisToLocalDate(millis: Long, format: String) =
        DateFormat.format(format, millis).toString()

    fun secsToDuration(secs : Int) = millisToDuration(secs.toLong() * 1000)

    fun millisToDuration(millis : Long) : String {
        return if (millis >= 60 * 60 * 1000)
            millisToUtcDate(millis, FORMAT_LONG_DURATION)
        else
            millisToUtcDate(millis, FORMAT_DURATION)
    }

    fun millisToUtcDateShort(millis: Long) = millisToUtcDate(millis, FORMAT_DATE_SHORT)

    private fun millisToUtcDate(millis: Long, format: String): String {
        val calendar = GregorianCalendar(SimpleTimeZone(0, "UTC"))
        calendar.timeInMillis = millis
        return (DateFormat.format(format, calendar)).toString()
    }

}