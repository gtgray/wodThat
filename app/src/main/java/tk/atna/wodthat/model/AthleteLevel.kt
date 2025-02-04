package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AthleteLevel(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)
