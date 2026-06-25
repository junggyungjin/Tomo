package ja.ko.tomo.feature.mypage

import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.model.User

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class Success(
        val user: User,
        val isLoggingOut: Boolean = false // 로그아웃 중 상태 관리 (중복 클릭 방지)
    ) : MyPageUiState
    data class Error(val message: UiText) : MyPageUiState
}