package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Wod(
    @Json(name = "id") val id: Int,
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "is_favorite") val savedInternal: Boolean?,
    @Json(name = "is_completed") val doneInternal: Boolean?,
    @Json(name = "is_private") val privateInternal: Boolean?,
    @Json(name = "tags") val tagsInternal: String,
    @Json(name = "headline") val headline: String,
    @Json(name = "general_scheme") val generalScheme: String?,
    @Json(name = "scheme_repeats") val schemeRepeats: Int,
    @Json(name = "exercises") val exercises: List<WodExercise>,
    @Json(name = "modality") val modality: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "muscles") val muscles: List<Muscle>?,
    @Json(name = "results_count") val resultsCount: Int,
    @Json(name = "my_ratings") val myRatings: String?,
    @Json(name = "summary_rating") val summaryRatingInternal: String?,
    @Json(name = "comments_count") val commentsCount: Int,
    @Json(name = "ratings_count") val ratingsCount: Int
) {

    val saved: Boolean
        get() = savedInternal ?: false

    val done: Boolean
        get() = doneInternal ?: false

    val tags: List<WodTag>
        get() = tagsInternal.split(",")
            .mapNotNull { item ->
                WodTag.getByValue(item)
            }

    val summaryRating: Float
        get() = (summaryRatingInternal ?: "0.0").toFloat()

}
