package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Gym(
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "logo") val logoLink: String?
)