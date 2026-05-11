package ja.ko.tomo.data.repository

import ja.ko.tomo.data.dto.SocialSignUpRequest
import ja.ko.tomo.data.mapper.toDomain
import ja.ko.tomo.data.remote.AuthService
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
): AuthRepository {
    override suspend fun signUpWithSocial(
        provider: String,
        idToken: String
    ): AuthResult {
        return try {
            // 1. API 호출
            val userDto = authService.signUpWithSocial(
                SocialSignUpRequest(provider = provider, idToken = idToken)
            )
            // 2. Mapper를 통해 DTO -> Domain 변환 후 성공 결과 반환
            AuthResult.Success(userDto.toDomain())
        }catch (e: Exception) {
            AuthResult.Error(e.message ?: "회원가입 중 알 수 없는 에러가 발생했습니다.")
        }
    }
}