package ja.ko.tomo.feature.feed.feedlist

import ja.ko.tomo.core.ui.util.UiText

/**
 * 피드 리스트 화면에서 발생하는 일회성 이벤트를 정의하는 인터페이스 (ADDED)
 */
sealed interface FeedUiEffect {
    data class NavigateToDetail(val feedId: String) : FeedUiEffect
    data object NavigateToCreateFeed : FeedUiEffect
    data class ShowSnackBar(val message: UiText) : FeedUiEffect
}