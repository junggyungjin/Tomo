package ja.ko.tomo.feature.auth

import androidx.annotation.StringRes
import ja.ko.tomo.core.ui.util.UiText

sealed interface AuthIntroUiEffect {
    data object NavigateToSignUp : AuthIntroUiEffect
    // 문의하기 화면(이메일 앱)으로 이동하는 액션
    data object NavigateToInquiry : AuthIntroUiEffect
    data class ShowSnackbar(val message: UiText): AuthIntroUiEffect
}