package ja.ko.tomo.domain.feed.usecase

import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.repository.FeedRepository
import javax.inject.Inject

/**
 * 새로운 피드를 생성하는 UseCase
 */
class CreateFeedUseCase @Inject constructor(
    private val repo: FeedRepository
) {
    suspend operator fun invoke(content: String?, hasCallRoom: Boolean): FeedResult {
        return repo.createdFeed(content, hasCallRoom)
    }
}