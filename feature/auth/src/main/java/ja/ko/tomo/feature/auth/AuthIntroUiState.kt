package ja.ko.tomo.feature.auth

sealed interface AuthIntroUiState {
    data object Loading : AuthIntroUiState
    data class Success(val isVideoLoading: Boolean = false) : AuthIntroUiState
    data object Empty : AuthIntroUiState
    data class Error(val message: String): AuthIntroUiState
}