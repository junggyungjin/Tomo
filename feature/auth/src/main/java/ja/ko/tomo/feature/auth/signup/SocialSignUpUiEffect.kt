package ja.ko.tomo.feature.auth.signup

import androidx.annotation.StringRes

interface SocialSignUpUiEffect {
    data object NavigateToNext : SocialSignUpUiEffect
    data object NavigateBack : SocialSignUpUiEffect
    data class ShowSnackbar(@StringRes val resId: Int): SocialSignUpUiEffect
    data class ShowToast(val message: String) : SocialSignUpUiEffect
}