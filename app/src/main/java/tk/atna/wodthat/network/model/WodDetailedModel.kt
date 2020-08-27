package tk.atna.wodthat.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tk.atna.wodthat.model.AthleteLevel
import tk.atna.wodthat.model.Author
import tk.atna.wodthat.model.WodExercise
import tk.atna.wodthat.model.WodTag

@JsonClass(generateAdapter = true)
data class WodDetailedModel(
    @Json(name = "id") val id: Int,
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "is_favorite") val isFavorite: Boolean?,
    @Json(name = "is_completed") val isCompleted: Boolean?,
    @Json(name = "is_private") val isPrivate: Boolean?,
    @Json(name = "tags") val tags: String,
    @Json(name = "headline") val headline: String,
    @Json(name = "general_scheme") val generalScheme: String?,
    @Json(name = "scheme_repeats") val schemeRepeats: Int,
    @Json(name = "exercises") val exercises: List<WodExercise>,
    @Json(name = "cover") val cover: String?,
    @Json(name = "modality") val modality: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "results_count") val resultsCount: Int,
    @Json(name = "my_ratings") val myRatings: String?,
    @Json(name = "summary_rating") val summaryRating: String?,
    @Json(name = "is_recommended") val isRecommended: Boolean?,
    @Json(name = "description") val description: String?,
    @Json(name = "video") val videoLink: String,
    @Json(name = "time_limit") val timeLimit: Int,
    @Json(name = "type_training") val typeTraining: Int,
    @Json(name = "sum_reps") val sumReps: Int,
    @Json(name = "round_reps") val roundReps: Int?,
    @Json(name = "number_rounds") val numberRounds: Int?,
    @Json(name = "comments_count") val commentsCount: Int,
    @Json(name = "ratings_count") val ratingsCount: Int,
    @Json(name = "skill_points") val skillPoints: Int,
    @Json(name = "athlete_level") val athleteLevel: AthleteLevel,
    @Json(name = "created_by") val author: Author
)

