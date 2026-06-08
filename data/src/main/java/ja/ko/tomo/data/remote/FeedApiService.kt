package ja.ko.tomo.data.remote

import ja.ko.tomo.data.dto.ApiResponse
import ja.ko.tomo.data.dto.request.CreateFeedRequest
import ja.ko.tomo.data.dto.response.FeedResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 피드 관련 API 통신을 담당하는 Retrofit 서비스 인터페이스 (ADDED)
 */
interface FeedApiService {
    /**
     * 새로운 피드 생성
     * @param request 피드 내용 및 통화방 생성 여부
     */
    @POST("feeds")
    suspend fun createFeed(
        @Body request: CreateFeedRequest
    ): ApiResponse<FeedResponseDto>

    /**
     * 전체 피드 목록 최신순 조회
     */
    @GET("feeds")
    suspend fun getFeeds(): ApiResponse<List<FeedResponseDto>>

    /**
     * 특정 피드 상세 정보 조회
     * @param feedId 피드 고유 ID
     */
    @GET("feeds/{id}")
    suspend fun getFeedById(
        @Path("id") feedId: String
    ): ApiResponse<FeedResponseDto>
}