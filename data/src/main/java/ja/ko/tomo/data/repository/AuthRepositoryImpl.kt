package ja.ko.tomo.data.repository

import ja.ko.tomo.data.dto.request.DevLoginRequest
import ja.ko.tomo.data.dto.request.LogoutRequest
import ja.ko.tomo.data.dto.request.SocialSignUpRequest
import ja.ko.tomo.data.local.TokenManager
import ja.ko.tomo.data.mapper.toDomain
import ja.ko.tomo.data.remote.AuthService
import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.log

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
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

                // 성공 시 로컬에 토큰 저장 (자동 로그인 활성화)
                tokenManager.saveTokens(
                    access = loginData.accessToken,
                    refresh = loginData.refreshToken
                )

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

    // 자동 로그인을 위한 로그인 상태 확인 함수
    override suspend fun isLoggedIn(): Boolean {
        var token: String? = null
        // Flow를 일회성으로 수집하여 확인
        tokenManager.accessToken.firstOrNull()?.let { token = it }
        return !token.isNullOrBlank()
    }

    override suspend fun logout(): AuthResult {
        return try {
            // 저장된 리프레시 토큰 가져오기
            val refreshToken = tokenManager.refreshToken.first() ?: ""

            // 서버 API 호출
            if (refreshToken.isNotBlank()) {
                authService.logout(LogoutRequest(refreshToken))
            }

            // 로컬 데이터 삭제 (서버 결과와 무관하게 수행하여 사용자 세션 종료)
            tokenManager.clearTokens()

            AuthResult.LogoutSuccess

        }catch (e: Exception) {
            Timber.tag("AuthRepo").e(e, "로그아웃 중 오류 발생")
            tokenManager.clearTokens()
            AuthResult.Error(e.message ?: "로그아웃 처리 중 오류가 발생했습니다.")
        }
    }

    override suspend fun devLogin(providerId: String): AuthResult {
        return try {
            val response = authService.devLogin(DevLoginRequest(providerId))

            if (response.success && response.data != null) {
                val loginData = response.data

                tokenManager.saveTokens(
                    access = loginData.accessToken,
                    refresh = loginData.refreshToken
                )

                AuthResult.Success(loginData.user.toDomain())
            }else {
                val errorBody = response.error
                Timber.tag("AuthRepo").e("개발 로그인 실패 code=${errorBody?.code}, message=${errorBody?.message}")
                AuthResult.Error(response.error?.message ?: "개발 로그인에 실패했습니다.")
            }
        }catch (e: Exception) {
            Timber.tag("AuthRepo").e(e, "개발 로그인 중 예외 발생")
            AuthResult.Error(e.message ?: "알 수 없는 에러가 발생했습니다.")
        }
    }
}