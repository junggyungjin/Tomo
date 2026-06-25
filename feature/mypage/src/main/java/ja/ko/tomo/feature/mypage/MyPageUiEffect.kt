package ja.ko.tomo.feature.mypage

import ja.ko.tomo.core.ui.util.UiText

sealed interface MyPageUiEffect {
    data object NavigateToAuth: MyPageUiEffect
    data class ShowSnackBar(val message: UiText) : MyPageUiEffect
}