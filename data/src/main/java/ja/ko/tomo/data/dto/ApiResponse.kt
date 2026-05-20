package ja.ko.tomo.data.dto

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val timestamp: String,
    val data: T? = null,
    val error: ApiErrorDto? = null
) {
}

@OptIn(InternalSerializationApi::class)
@Serializable
data class ApiErrorDto(
    val code: String,
    val message: String,
    val details: List<String>? = null
)