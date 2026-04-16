package ja.ko.tomo.feature.meeting.meeting

import ja.ko.tomo.domain.model.Meeting

sealed interface TomoMeetingUiState{
    data object Loading: TomoMeetingUiState
    data object Empty : TomoMeetingUiState
    data class Error(val message: String) : TomoMeetingUiState
    data class Success(
        val meeting: Meeting,
        val buttonText: String,
        val isJoinEnabled: Boolean
    ) : TomoMeetingUiState
}