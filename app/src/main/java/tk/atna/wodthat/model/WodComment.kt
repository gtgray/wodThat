package tk.atna.wodthat.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class WodComment(
    @Json(name = "id") val id: Int,
    @Json(name = "comment") val text: String,
    @Json(name = "created_by") val author: Author,
    @Json(name = "created_at") val createdAt: Date
)

