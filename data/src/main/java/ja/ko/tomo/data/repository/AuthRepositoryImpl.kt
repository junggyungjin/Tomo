package ja.ko.tomo.data.repository

import ja.ko.tomo.data.dto.SocialSignUpRequest
import ja.ko.tomo.data.mapper.toDomain
import ja.ko.tomo.data.remote.AuthService
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.model.User
import ja.ko.tomo.domain.repository.AuthRepository
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

            // TODO: response.accessToken과 refreshToken을 로컬(EncryptedSharedPreferences 등)에 저장하는 로직이 여기에 추가

            // 2. Mapper를 통해 DTO -> Domain 변환 후 성공 결과 반환
//            AuthResult.Success(response.user.toDomain())

            // 현재 서버가 user 정보를 주지 않으므로,
            // 1. AuthResponseDto에서 user를 nullable(?)로 바꾸고
            // 2. 여기서 null 체크를 하거나 빈 객체를 넘겨줘야 합니다.
            // 예시: 유저 정보가 없어도 일단 성공으로 간주하고 화면을 넘길 경우
            AuthResult.Success(
                // 만약 response.user가 null이면 임시 데이터를 생성하거나
                // 서버 응답 규격(AuthResponseDto)을 먼저 수정해야 합니다.
                response.user?.toDomain() ?: User(
                    id = -1L,
                    email = email ?: "asdf@asdf",
                    nickname = name ?: "User",
                    profileImageUrl = null,
                    introduction = null
                )
            )
        }catch (e: Exception) {
            AuthResult.Error(e.message ?: "회원가입 중 알 수 없는 에러가 발생했습니다.")
        }
    }
}