package ja.ko.tomo.domain.feed.repository

import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.model.FeedFilter

interface FeedRepository {
    suspend fun getFeeds(filter: FeedFilter): FeedResult
    suspend fun createdFeed(content: String?, hasCallRoom: Boolean): FeedResult
    suspend fun getFeedById(id: String): FeedResult
    suspend fun toggleLike(feedId: String): FeedResult
}