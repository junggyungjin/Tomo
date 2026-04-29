package ja.ko.tomo.feature.meeting.meetingdetail

import androidx.annotation.StringRes
import ja.ko.tomo.domain.model.Meeting

sealed interface MeetingDetailUiState {
    data object Loading : MeetingDetailUiState
    data class Error(val message: String) : MeetingDetailUiState
    data class Success(
        val meeting: Meeting,
        @StringRes val buttonTextRes: Int,
        val isButtonEnabled: Boolean
    ) : MeetingDetailUiState
}