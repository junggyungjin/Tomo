package ja.ko.tomo.data.remote

import ja.ko.tomo.data.dto.ApiResponse
import ja.ko.tomo.data.dto.request.UpdateUserProfileRequestDto
import ja.ko.tomo.data.dto.response.AuthUserDto
import ja.ko.tomo.data.dto.response.UpdateUserProfileResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApiService {
    @GET("users/me")
    suspend fun getMyInfo(): ApiResponse<UpdateUserProfileResponseDto>
    @PATCH("/users/profile")
    suspend fun updateUserProfile(
        @Body request: UpdateUserProfileRequestDto
    ): ApiResponse<UpdateUserProfileResponseDto>
}