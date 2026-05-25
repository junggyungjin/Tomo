package ja.ko.tomo.data.repository

import ja.ko.tomo.data.dto.request.SocialSignUpRequest
import ja.ko.tomo.data.mapper.toDomain
import ja.ko.tomo.data.remote.AuthService
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.repository.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
): AuthRepository {
    override suspend fun signUpWithSocial(
        provider: String,
        token: String,
        providerId: String,
        email: String?,
        name: String?
    ): AuthResult {
        return try {
            // 1. API 호출
            val response = authService.signUpWithSocial(
                provider = provider,
                SocialSignUpRequest(
                    token = token,
                    providerId = providerId,
                    email = email,
                    name = name
                )
            )

            // 2. Mapper를 통해 DTO -> Domain 변환 후 성공 결과 반환
            if (response.success && response.data != null) {
                val loginData = response.data

                // TODO: response.accessToken과 refreshToken을 로컬(EncryptedSharedPreferences 등)에 저장하는 로직이 여기에 추가

                AuthResult.Success(loginData.user.toDomain())
            }else {
                val errorBody = response.error
                Timber.tag("AuthRepo").e("API 실패: code=${errorBody?.code}, message=${errorBody?.message}")

                AuthResult.Error(response.error?.message ?: "서버 응답 오류가 발생했습니다.")
            }

        }catch (e: Exception) {
            Timber.tag("AuthRepo").e(e, "소셜 로그인 중 예상치 못한 예외 발생")

            AuthResult.Error(e.message ?: "회원가입 중 알 수 없는 에러가 발생했습니다.")
        }
    }
}