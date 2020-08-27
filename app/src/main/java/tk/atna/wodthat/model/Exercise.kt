package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Exercise(
    @Json(name = "id") val id: Int,
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
//    val localname: String,
    @Json(name = "modality") val modality: String,
    @Json(name = "class_exercise") val classExercise: String,
    @Json(name = "equipment_type") val equipment: String,
    @Json(name = "level") val level: String,
    @Json(name = "video") val videoLink: String,
    @Json(name = "teaser") val teaser: String,
    @Json(name = "abbreviation") val abbreviation: String
) {
}

