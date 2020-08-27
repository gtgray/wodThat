package tk.atna.wodthat.model

import com.squareup.moshi.Json

enum class WodTag(private val value: String) {

    @Json(name = "Hero") HERO("Hero"),
    @Json(name = "Personal") PERSONAL("Personal"),
    @Json(name = "Team") TEAM("Team"),
    @Json(name = "Singlet") SINGLET("Singlet"),
    @Json(name = "Couplet") COUPLET("Couplet"),
    @Json(name = "Triplet") TRIPLET("Triplet"),
    @Json(name = "For-Time") FOR_TIME("For-Time"),
    @Json(name = "AMRAP") AMRAP("AMRAP"),
    @Json(name = "EMOM") EMOM("EMOM"),
    @Json(name = "Bodyweight") BODYWEIGHT("Bodyweight"),
    @Json(name = "Kettlebell") KETTLEBELL("Kettlebell"),
    @Json(name = "Rower") ROWER("Rower"),
    @Json(name = "Benchmark") BENCHMARK("Benchmark"),
    @Json(name = "Skill") SKILL("Skill"),
    @Json(name = "Weightlifting") WEIGHTLIFTING("Weightlifting"),
    @Json(name = "Strength") STRENGTH("Strength"),
    @Json(name = "Endurance") ENDURANCE("Endurance"),
    @Json(name = "Open") OPEN("Open"),
    @Json(name = "Crossfit.com") CROSSFIT_COM("Crossfit.com"),
    @Json(name = "Girls") GIRLS("Girls"),
    ;

    companion object {
        fun getByValue(value: String): WodTag? =
            values().firstOrNull { item -> item.value == value }
    }

}