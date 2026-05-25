package ja.ko.tomo.data.dto.request

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class UpdateUserProfileRequestDto(
    val userId: String,
    val nickname: String,
    val nationality: String,
    val gender: String,
    val profileImageUrl: String?
)