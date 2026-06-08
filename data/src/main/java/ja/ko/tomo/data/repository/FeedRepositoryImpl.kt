package ja.ko.tomo.data.repository

import ja.ko.tomo.data.dto.request.CreateFeedRequest
import ja.ko.tomo.data.mapper.toDomain
import ja.ko.tomo.data.remote.FeedApiService
import ja.ko.tomo.domain.feed.model.FeedResult
import ja.ko.tomo.domain.feed.repository.FeedRepository
import timber.log.Timber
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedApiService: FeedApiService
) : FeedRepository {
    override suspend fun getFeeds(): FeedResult {
        return try {
            val response = feedApiService.getFeeds()

            if (response.success && response.data != null) {
                val feeds = response.data.map { it.toDomain() }
                if (feeds.isEmpty()) {
                    FeedResult.Empty
                } else {
                    FeedResult.Success(feeds)
                }
            }else {
                val errorBody = response.error
                Timber.tag("FeedRepo").e("피드 목록 조회 실패: code=${errorBody?.code}, message=${errorBody?.message}")
                FeedResult.Error(response.error?.message ?: "피드 목록을 가져오는데 실패했습니다.")
            }
        }catch (e: Exception) {
            Timber.tag("FeedRepo").e(e, "피드 목록 조회 중 예상치 못한 예외 발생")
            FeedResult.Error(e.message ?: "알 수 없는 에러가 발생했습니다.")
        }
    }

    override suspend fun createdFeed(
        content: String?,
        hasCallRoom: Boolean
    ): FeedResult {
        return try {
            val response = feedApiService.createFeed(
                CreateFeedRequest(content = content, hasCallRoom = hasCallRoom)
            )
            if (response.success && response.data != null) {
                FeedResult.SingleSuccess(response.data.toDomain())
            }else {
                val errorBody = response.error
                Timber.tag("FeedRepo").e("피드 생성 실패: code=${errorBody?.code}, message=${errorBody?.message}")
                FeedResult.Error(response.error?.message ?: "피드 생성에 실패했습니다.")
            }
        }catch (e: Exception) {
            Timber.tag("FeedRepo").e(e, "피드 생성 중 예상치 못한 예외 발생")
            FeedResult.Error(e.message ?: "피드 생성 중 알 수 없는 에러가 발생했습니다.")
        }
    }

    override suspend fun getFeedById(id: String): FeedResult {
        return try {
            val response = feedApiService.getFeedById(feedId = id)

            if (response.success && response.data != null) {
                FeedResult.SingleSuccess(response.data.toDomain())
            }else {
                val errorBody = response.error
                Timber.tag("FeedRepo").e("피드 상세 조회 실패: id=$id, code=${errorBody?.code}, message=${errorBody?.message}")
                FeedResult.Error(response.error?.message ?: "피드 정보를 가져오는데 실패했습니다.")
            }
        }catch (e: Exception) {
            Timber.tag("FeedRepo").e(e, "피드 상세 조회 중 예외 발생")
            FeedResult.Error(e.message ?: "알 수 없는 에러가 발생했습니다.")
        }
    }
}