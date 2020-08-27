package tk.atna.wodthat.stuff


private const val YOUTUBE_URL_REGEXP = "(?:youtube(?:-nocookie)?\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})"
private const val DEFAULT_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/0.jpg"

fun discoverVideoId(url: String?): String? {
    url ?: return null
    var videoId: String? = null
    val matcher = YOUTUBE_URL_REGEXP.toPattern().matcher(url)
    if(matcher.find())
        videoId = matcher.group(1)
    //
    return videoId
}

fun discoverThumbnailUrl(videoId: String?): String? {
    videoId ?: return null
    return String.format(DEFAULT_THUMBNAIL_URL, videoId)
}
