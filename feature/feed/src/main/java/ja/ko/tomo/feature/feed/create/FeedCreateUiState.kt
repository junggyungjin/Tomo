package ja.ko.tomo.feature.feed.create

import ja.ko.tomo.core.ui.util.UiText

/**
 * 피드 작성 화면의 UI 상태
 */
sealed interface FeedCreateUiState{
    data object Loading : FeedCreateUiState
    data class Success(
        val content: String = "",
        val hasCallRoom: Boolean = false, // 통화방 생성 체크 여부
        val isSubmitting: Boolean = false, // 저장 버튼 클릭 후 서버 통신 중일 때
        val error: UiText? = null // 입력 폼 관련 에러 메시지
    ) : FeedCreateUiState {
        // 입력 값이 유효한지 확인 (내용이 있거나 통화방을 생성하거나)
        val canSubmit: Boolean get() = (content.isNotBlank() || hasCallRoom) && !isSubmitting
    }

    // 화면 로드 자체가 실패했을 때
    data class Error(val message: UiText) : FeedCreateUiState
}