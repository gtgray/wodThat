package tk.atna.wodthat.model

import com.squareup.moshi.JsonClass

enum class ExerciseUnit(private val value: String) {

    POOD("pood"),
    ;

    fun getByValue(value: String): ExerciseUnit? = values().firstOrNull { this.value == value }

}