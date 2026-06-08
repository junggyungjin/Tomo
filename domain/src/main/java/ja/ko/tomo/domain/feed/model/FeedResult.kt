package ja.ko.tomo.domain.feed.model

sealed interface FeedResult {
    data class Success(val feeds: List<Feed>) : FeedResult
    data class SingleSuccess(val feed: Feed) : FeedResult
    data object Empty : FeedResult
    data class Error(val message: String) : FeedResult
}