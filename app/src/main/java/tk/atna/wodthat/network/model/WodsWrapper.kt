package tk.atna.wodthat.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import tk.atna.wodthat.model.Wod


@JsonClass(generateAdapter = true)
data class WodsWrapper(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<Wod>
)
