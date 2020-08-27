package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExerciseDetailed(
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "modality") val modality: String,
    @Json(name = "class_exercise") val classExercise: String,
    @Json(name = "equipment_type") val equipment: String,
    @Json(name = "level") val level: String,
    @Json(name = "description") val description: String,
    @Json(name = "video") val videoLink: String,
    @Json(name = "teaser") val teaser: String,
    @Json(name = "max_group_muscle") val maxGroupMuscle: List<Muscle> = listOf(),
    @Json(name = "mid_group_muscle") val midGroupMuscle: List<Muscle> = listOf(),
    @Json(name = "min_group_muscle") val minGroupMuscle: List<Muscle> = listOf(),
    @Json(name = "muscles") val muscles: List<Muscle> = listOf(),
    @Json(name = "abbreviation") val abbreviation: String
) {
}

