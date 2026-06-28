package ja.ko.tomo.domain.feed.usecase

import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.repository.FeedRepository
import ja.ko.tomo.domain.model.FeedFilter
import javax.inject.Inject

/**
 * 전체 피드 목록을 조회하는 UseCase
 */
class GetFeedsUseCase @Inject constructor(
    private val repo: FeedRepository
) {
    suspend operator fun invoke(filter: FeedFilter): FeedResult = repo.getFeeds(filter)
}