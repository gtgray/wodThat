package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "id") val id: Int,
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "flag") val flagLink: String
)