package ja.ko.tomo.core.ui.util

import ja.ko.tomo.core.ui.R
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 프로젝트 전체에서 공통으로 사용하는 에러 매핑 확장 함수
 */
fun Throwable.toUiText(): UiText {
    Timber.e(this, "API_ERROR_LOG")

    return when (this) {
        // 1. 네트워크 연결 자체가 없는 경우 (DNS 에러 등)
        is UnknownHostException, is ConnectException -> {
            UiText.StringResource(R.string.error_network)
        }

        // 2. 서버 응답 시간이 초과된 경우
        is SocketTimeoutException -> {
            UiText.StringResource(R.string.error_timeout)
        }

        // 3. 기타 입출력 에러 (네트워크 끊김 등)
        is IOException -> {
            UiText.StringResource(R.string.error_network)
        }

        // 4. 그 외 정의 되지 않은 에러
        else ->  {
            // 메시지가 있으면 보여주고 없으면 "알 수 없는 에러" 출력
            val message = this.localizedMessage
            if (message.isNullOrBlank()) {
                UiText.StringResource(R.string.error_unknown)
            } else {
                UiText.DynamicString(message)
            }
        }
    }
}