package tk.atna.wodthat.model

import com.squareup.moshi.Json

data class TShortSize(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
) {
}