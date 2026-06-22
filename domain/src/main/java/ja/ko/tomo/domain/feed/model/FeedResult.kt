package ja.ko.tomo.domain.feed.model

sealed interface FeedResult {
    data class Success(val feeds: List<Feed>) : FeedResult
    data class SingleSuccess(val feed: Feed) : FeedResult
    data class LikeSuccess(
        val feedId: String,
        val isLiked: Boolean,
        val likeCount: Int
    ): FeedResult
    data object Empty : FeedResult
    data class Error(val message: String) : FeedResult
}