package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "id") val id: Int,
    @Json(name = "slug") val code: String,
    @Json(name = "name") val name: String
)