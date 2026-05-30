package ja.ko.tomo.data.remote

import dagger.Lazy
import ja.ko.tomo.data.dto.request.TokenRefreshRequest
import ja.ko.tomo.data.local.AuthEventHelper
import ja.ko.tomo.data.local.TokenManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: Lazy<AuthService>, // 순환 참조 방지를 위해 Lazy 주입
    private val authEventHelper: AuthEventHelper
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // 무한루프 방지 로직
        // 이미 리프레시 API(/auth/refresh)를 호출했는데도 401이 왔다면
        // 리프레시 토큰 자체가 만료된 것이므로 더 이상 재시도하지 않고 세션 만료 처리를 함
        if (response.request.url.encodedPath.endsWith("auth/refresh")) {
            runBlocking {
                authEventHelper.emitSessionExpired()
            }
            return null
        }

        if (response.code != 401) return null

        // 1. refresh token 가져오기
        val refreshToken = runBlocking { tokenManager.refreshToken.firstOrNull() } ?: return null

        // 2. 서버에 토큰 갱신 요청 (동기 실행)
        val refreshResult = runBlocking {
            try {
                authService.get().refreshAccessToken(TokenRefreshRequest(refreshToken))
            }catch (e: Exception) {
                null
            }
        }

        // 3. 성공 시 토큰 저장 및 기존 요청 재시도
        return if (refreshResult?.success == true && refreshResult.data != null) {
            val newData = refreshResult.data
            runBlocking {
                tokenManager.saveTokens(newData.accessToken, newData.refreshToken)
            }

            response.request.newBuilder()
                .header("Authorization", "Bearer ${newData.accessToken}")
                .build()
        } else {
            runBlocking { authEventHelper.emitSessionExpired() }
            null
        }
    }
}