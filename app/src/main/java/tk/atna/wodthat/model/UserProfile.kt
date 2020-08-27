package tk.atna.wodthat.model

import com.squareup.moshi.Json
import java.util.*

data class UserProfile(
    @Json(name = "score") val score: Int,
    @Json(name = "birth_date") val birthDate: Date? = null,
    @Json(name = "username") val username: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String = "",
    @Json(name = "avatar") val avatarLink: String,
    @Json(name = "cover") val coverLink: String,
    @Json(name = "sex") val sex: Int,
    @Json(name = "start_crossfit") val startCrossfit: String,
    @Json(name = "about") val about: String,
    @Json(name = "fb") val fb: String,
    @Json(name = "vk") val vk: String,
    @Json(name = "tw") val tw: String,
    @Json(name = "ig") val ig: String,
    @Json(name = "tg") val tg: String,
    @Json(name = "athlete_level") val athleteLevel: AthleteLevel,
    @Json(name = "t_short_size") val tShortSize: TShortSize,
    @Json(name = "country") val country: Country,
    @Json(name = "city") val city: City,
    @Json(name = "gym") val gym: Gym,
    @Json(name = "address") val address: String = "",
    @Json(name = "zip_code") val zipCode: String = "",
    @Json(name = "phone") val phone: String = "",
    @Json(name = "privacy_policy") val privacyPolicy: Boolean,
    @Json(name = "personal_data") val personalData: Boolean,
    @Json(name = "skill_points") val skillPints: Int,
    @Json(name = "height") val height: Float,
    @Json(name = "weight") val weight: Float,
    @Json(name = "default_tp") val defaultTp: DefaultTp,
    @Json(name = "is_subscribe") val subscribe: Boolean,
    @Json(name = "is_friend") val friend: Boolean
) {
}