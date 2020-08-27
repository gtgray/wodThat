package tk.atna.wodthat.model

import java.util.*

data class WodResult(
    val id: Int,
    val wodId: String,
    val wodCode: String,
    val seconds: Int,
    val rxd: Boolean,
    val country: Country?,
    val city: City?,
    val reps: Int,
    val weight: Float,
    val timeLimit: Int,
    val repsPerRound: Int,
    val roundsCount: Int,
    val completedAt: Date,
    val gym: Gym?,
    val video: Boolean,
    val description: String,
    val commentsCount: Int?,
    val reportsCount: Int,
    val reported: Boolean?,
    val skillPoints: Int,
    val skillWod: Int,
    val athleteLevel: AthleteLevel,
    val rank: Int,
    val my: Boolean,
    val friend: Boolean,
    val subscribe: Boolean,
    val author: Author
)
