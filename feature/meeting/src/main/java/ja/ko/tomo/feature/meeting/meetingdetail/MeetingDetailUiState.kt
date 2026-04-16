package ja.ko.tomo.feature.meeting.meetingdetail

import ja.ko.tomo.domain.model.Meeting

sealed interface MeetingDetailUiState {
    data object Loading : MeetingDetailUiState
    data class Error(val message: String) : MeetingDetailUiState
    data class Success(
        val meeting: Meeting,
        val buttonText: String,
        val isButtonEnabled: Boolean
    ) : MeetingDetailUiState
}