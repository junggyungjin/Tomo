package ja.ko.tomo.domain.feed.usecase

import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.repository.FeedRepository
import javax.inject.Inject

/**
 * 특정 ID의 피드 상세 정보를 조회하는 UseCase
 */
class GetFeedByIdUseCase @Inject constructor(
    private val repo: FeedRepository
) {
    suspend operator fun invoke(id: String): FeedResult = repo.getFeedById(id)
}