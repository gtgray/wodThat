package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "username") val username: String?,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "sex") val gender: Int?,
    @Json(name = "avatar") val avatarLink: String?
)