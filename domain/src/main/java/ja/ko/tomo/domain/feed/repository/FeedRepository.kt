package ja.ko.tomo.domain.feed.repository

import ja.ko.tomo.domain.feed.model.FeedResult

interface FeedRepository {
    suspend fun getFeeds(): FeedResult
    suspend fun createdFeed(content: String?, hasCallRoom: Boolean): FeedResult
    suspend fun getFeedById(id: String): FeedResult
}