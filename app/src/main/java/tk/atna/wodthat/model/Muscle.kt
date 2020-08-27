package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Muscle(
    @Json(name = "name") val name: String,
    @Json(name = "color") val color: MuscleColor? = null
) {
}

enum class MuscleColor(val value: String) {

    @Json(name = "Red") RED("Red"),
    @Json(name = "Yellow") YELLOW("Yellow"),
    @Json(name = "Green") GREEN("Green"),
    ;

    fun getByValue(value: String): MuscleColor? = values().firstOrNull { this.value == value }

}