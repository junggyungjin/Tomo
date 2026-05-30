package ja.ko.tomo.data.remote

import ja.ko.tomo.data.dto.ApiResponse
import ja.ko.tomo.data.dto.response.AuthLoginResponseData
import ja.ko.tomo.data.dto.request.SocialSignUpRequest
import ja.ko.tomo.data.dto.request.TokenRefreshRequest
import ja.ko.tomo.data.dto.response.TokenRefreshResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("auth/login/{provider}")
    suspend fun signUpWithSocial(
        @Path("provider") provider: String,
        @Body request: SocialSignUpRequest
    ): ApiResponse<AuthLoginResponseData>

    @POST("auth/refresh")
    suspend fun refreshAccessToken(
        @Body request: TokenRefreshRequest
    ): ApiResponse<TokenRefreshResponseDto>
}