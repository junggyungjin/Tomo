package ja.ko.tomo.data.remote

import ja.ko.tomo.data.dto.SocialSignUpRequest
import ja.ko.tomo.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/social-signup")
    suspend fun signUpWithSocial(
        @Body request: SocialSignUpRequest
    ): UserDto
}