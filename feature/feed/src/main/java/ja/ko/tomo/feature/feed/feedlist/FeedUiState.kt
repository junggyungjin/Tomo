package ja.ko.tomo.feature.feed.feedlist

import ja.ko.tomo.core.ui.util.UiText
import ja.ko.tomo.domain.feed.model.Feed
import ja.ko.tomo.domain.model.FeedFilter

/**
 * 피드 리스트 화면의 UI 상태를 정의하는 인터페이스 (ADDED)
 */
sealed interface FeedUiState {
    data object Loading : FeedUiState
    data class Success(
        val feeds: List<Feed> = emptyList(),
        val activeCallRooms: List<Feed> = emptyList(),
        val selectedFilter: FeedFilter = FeedFilter.ALL,
        val isRefreshing: Boolean = false
    ) : FeedUiState
    data class Error(val message: UiText) : FeedUiState
}