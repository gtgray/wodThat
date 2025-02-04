package tk.atna.wodthat.model

data class WodDetailed(
    val id: Int,
    val code: String,
    val name: String,
    val saved: Boolean,
    val done: Boolean,
    val private: Boolean,
    val tags: String,
    val tagsList: List<WodTag>? = null,
    val headline: String,
    val generalScheme: String?,
    val schemeRepeats: Int,
    val exercises: List<WodExercise>,
    val cover: String?,
    val modality: String,
    val duration: Int,
    val resultsCount: Int,
    val results: List<WodResult>,
    val myRatings: String?,
    val summaryRating: Float,
    val recommended: Boolean,
    val description: String,
    val videoLink: String,
    val timeLimit: Int,
    val typeTraining: Int,
    val sumReps: Int,
    val roundReps: Int,
    val numberRounds: Int,
    val commentsCount: Int,
    val comments: List<WodComment>,
    val ratingsCount: Int,
    val skillPoints: Int,
    val athleteLevel: AthleteLevel,
    val author: Author
)

