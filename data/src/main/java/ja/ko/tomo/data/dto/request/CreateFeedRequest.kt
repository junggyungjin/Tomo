package ja.ko.tomo.data.dto.request

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

/**
 * 피드 생성을 위한 요청 객체
 * @property content 피드 내용 (선택)
 * @property hasCallRoom 통화방 생성 여부
 */
@OptIn(InternalSerializationApi::class)
@Serializable
data class CreateFeedRequest(
    val content: String? = null,
    val hasCallRoom: Boolean
) {
}