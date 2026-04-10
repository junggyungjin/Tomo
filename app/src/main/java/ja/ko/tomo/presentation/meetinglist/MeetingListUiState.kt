package ja.ko.tomo.presentation.meetinglist

import ja.ko.tomo.domain.model.Meeting

sealed interface MeetingListUiState {
    data object Loading: MeetingListUiState
    data object Empty : MeetingListUiState
    data class Error(val message: String) : MeetingListUiState
    data class Success(
        val meetings: List<Meeting>,
        val selectedFilter: MeetingListFilter,
        val searchQuery: String = ""
    ) : MeetingListUiState
}