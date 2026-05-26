package ja.ko.tomo.feature.auth.signup

import androidx.annotation.StringRes
import ja.ko.tomo.core.ui.util.UiText

sealed interface SocialSignUpUiEffect {
    data object NavigateToNext : SocialSignUpUiEffect
    data object NavigateBack : SocialSignUpUiEffect
    data class NavigateToProfileSetup(val userId: String) : SocialSignUpUiEffect
    data class ShowSnackbar(val message: UiText): SocialSignUpUiEffect
    data class ShowToast(val message: String) : SocialSignUpUiEffect
}