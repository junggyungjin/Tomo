package ja.ko.tomo.domain.feed.usecase

import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.repository.FeedRepository
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val repo: FeedRepository
) {
    suspend operator fun invoke(feedId: String): FeedResult = repo.toggleLike(feedId)
}