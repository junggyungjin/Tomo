package ja.ko.tomo.feature.mypage

import ja.ko.tomo.domain.model.User

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class Success(val user: User) : MyPageUiState
    data class Error(val message: String) : MyPageUiState
}