package ja.ko.tomo.feature.auth.onboading

import ja.ko.tomo.core.ui.util.UiText

sealed interface ProfileSetupUiEffect {
    data object NavigateToHome : ProfileSetupUiEffect
    data object NavigateBack : ProfileSetupUiEffect
    data class ShowSnackbar(val message: UiText) : ProfileSetupUiEffect
}