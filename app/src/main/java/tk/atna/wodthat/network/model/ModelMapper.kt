package tk.atna.wodthat.network.model

import tk.atna.wodthat.model.*
import java.util.*


fun toWodDetailed(
    details: WodDetailedModel,
    comments: List<WodComment>,
    results: List<WodResultModel>
): WodDetailed {
    return WodDetailed(
        id = details.id,
        code = details.code,
        name = details.name,
        saved = details.isFavorite ?: false,
        done = details.isCompleted ?: false,
        private = details.isPrivate ?: false,
        tags = details.tags,
//        tagsList = details.tags
//            .split(",")
//            .mapNotNull { item -> WodTag.getByValue(item) },
        headline = details.headline,
        generalScheme = details.generalScheme,
        schemeRepeats = details.schemeRepeats,
        exercises = details.exercises,
        cover = details.cover,
        modality = details.modality,
        duration = details.duration,
        resultsCount = details.resultsCount,
        results = convert(results, details),
        myRatings = details.myRatings,
        summaryRating = (details.summaryRating ?: "0.0").toFloat(),
        recommended = details.isRecommended ?: false,
        description = details.description ?: "",
        videoLink = details.videoLink,
        timeLimit = details.timeLimit,
        typeTraining = details.typeTraining,
        sumReps = details.sumReps,
        roundReps = details.roundReps ?: 0,
        numberRounds = details.numberRounds ?: 0,
        commentsCount = details.commentsCount,
        comments = comments,
        ratingsCount = details.ratingsCount,
        skillPoints = details.skillPoints,
        athleteLevel = details.athleteLevel,
        author = details.author
    )
}

private fun convert(source: List<WodResultModel>, details: WodDetailedModel): List<WodResult> {
    return source.map { result ->
        WodResult(
            id = result.id,
            wodId = result.wodId,
            wodCode = result.wodCode,
            seconds = result.seconds ?: 0,
            rxd = result.rxd,
            country = result.country,
            city = result.city,
            reps = result.reps ?: 0,
            weight = if (details.typeTraining == 2) (result.reps ?: 0).toFloat() else 0f,
            timeLimit = details.timeLimit,
            repsPerRound = discoverRepsPerRound(details),
            roundsCount = discoverRoundsCount(details),
            completedAt = result.completedAt,
            gym = result.gym,
            video = result.video,
            description = result.description,
            commentsCount = result.commentsCount,
            reportsCount = result.reportsCount,
            reported = result.reported,
            skillPoints = result.skillPoints,
            skillWod = result.skillWod,
            athleteLevel = result.athleteLevel,
            rank = result.rank ?: 0,
            my = result.my ?: false,
            friend = result.friend,
            subscribe = result.subscribe,
            author = result.author
        )
    }
}

private fun discoverRepsPerRound(details: WodDetailedModel): Int {
    return when(details.typeTraining) {
        // 'for time' 1 (null) or more rounds
        0 -> details.sumReps / (details.numberRounds ?: 1)
        // 'AMRAP'
        1 -> details.roundReps ?: 0
        // 'Strength'
        2 -> -1
        // 'EMOM'
        3 -> details.roundReps ?: 0
        // any other unknown
        else -> -1
    }
}

private fun discoverRoundsCount(details: WodDetailedModel): Int {
    return when(details.typeTraining) {
        // 'for time' 1 (null) or more rounds
        0 -> details.numberRounds ?: 1
        // 'AMRAP'
        1 -> 0
        // 'Strength'
        2 -> details.numberRounds ?: 0
        // 'EMOM'
        3 -> 0
        // any other unknown
        else -> -1
    }
}





