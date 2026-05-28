package ja.ko.tomo.data.dto.response

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class UpdateUserProfileResponseDto(
    val id: String,
    val nickname: String,
    val handle: String,
    val nationality: String,
    val profileImageUrl: String?,
    val status: String
) {
}