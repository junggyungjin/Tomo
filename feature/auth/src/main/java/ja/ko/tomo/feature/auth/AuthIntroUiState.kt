package ja.ko.tomo.feature.auth

import ja.ko.tomo.core.ui.util.UiText

sealed interface AuthIntroUiState {
    data object Loading : AuthIntroUiState
    data class Success(val isVideoLoading: Boolean = false) : AuthIntroUiState
    data object Empty : AuthIntroUiState
    data class Error(val message: UiText): AuthIntroUiState
}