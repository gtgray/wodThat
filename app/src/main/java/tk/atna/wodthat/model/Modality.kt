package tk.atna.wodthat.model

import com.squareup.moshi.JsonClass

enum class Modality(private val value: String) {

    G("Gymnastics"),
    W("Weightlifting"),
    M("Cardio"),
    ;

    fun getByValue(value: String): Modality? = values().firstOrNull { this.value == value }

}