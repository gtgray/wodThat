package tk.atna.wodthat.model

import com.squareup.moshi.Json

data class DefaultTp(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "count_trainings") val countTrainings: Int,
    @Json(name = "joined") val joined: Boolean
) {
}