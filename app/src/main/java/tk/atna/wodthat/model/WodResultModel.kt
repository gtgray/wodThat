package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*


@JsonClass(generateAdapter = true)
data class WodResultModel(
    @Json(name = "id") val id: Int,
    @Json(name = "wod") val wodId: String,
    @Json(name = "wod_slug") val wodCode: String,
    @Json(name = "seconds") val seconds: Int?,
    @Json(name = "rxd") val rxd: Boolean,
    @Json(name = "country") val country: Country?,
    @Json(name = "city") val city: City?,
    @Json(name = "reps") val reps: Int?,
    @Json(name = "completed_at") val completedAt: Date,
    @Json(name = "gym") val gym: Gym?,
    @Json(name = "video") val video: Boolean,
    @Json(name = "description") val description: String,
    @Json(name = "comments_count") val commentsCount: Int?,
    @Json(name = "reports_count") val reportsCount: Int,
    @Json(name = "is_reported") val reported: Boolean?,
    @Json(name = "skill_points") val skillPoints: Int,
    @Json(name = "skill_wod") val skillWod: Int,
    @Json(name = "athlete_level") val athleteLevel: AthleteLevel,
    @Json(name = "rank") val rank: Int?,
    @Json(name = "is_my") val my: Boolean?,
    @Json(name = "is_friend") val friend: Boolean,
    @Json(name = "is_subscribe") val subscribe: Boolean,
    @Json(name = "created_by") val author: Author
)