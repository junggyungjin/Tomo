package ja.ko.tomo.feature.auth

import androidx.annotation.StringRes

sealed interface AuthIntroUiEffect {
    // 문의하기 화면(이메일 앱)으로 이동하는 액션
    data object NavigateToInquiry : AuthIntroUiEffect
    data class ShowSnackbar(@StringRes val resId: Int): AuthIntroUiEffect
}