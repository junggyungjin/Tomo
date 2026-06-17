package ja.ko.tomo.feature.feed.create

import ja.ko.tomo.core.ui.util.UiText

/**
 * 피드 작성 화면의 일회성 이벤트
 */
sealed interface FeedCreateUiEffect {
    data class NavigateBack(val isSuccess: Boolean = false) : FeedCreateUiEffect
    data class ShowSnackBar(val message: UiText) : FeedCreateUiEffect
}