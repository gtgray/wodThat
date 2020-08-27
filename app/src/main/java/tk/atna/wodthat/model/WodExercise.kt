package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.DecimalFormat


@JsonClass(generateAdapter = true)
data class WodExercise(
    @Json(name = "id") val id: Int,
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "repetitions") val repsScheme: String?,
    @Json(name = "man") val manWeightInternal: String?,
    @Json(name = "woman") val womanWeightInternal: String?,
    @Json(name = "units") val units: String?
) {

    val manWeight: String?
        get() = fixWeight(manWeightInternal)

    val womanWeight: String?
        get() = fixWeight(womanWeightInternal)


    private fun fixWeight(source: String?): String? {
        return when(source) {
            "0,0" -> "0"
            "0,5" -> "0.5"
            "1,0" -> "1"
            "1,5" -> "1.5"
            "2,0" -> "2"
            "2,5" -> "2.5"
            else -> source
        }
    }


}

