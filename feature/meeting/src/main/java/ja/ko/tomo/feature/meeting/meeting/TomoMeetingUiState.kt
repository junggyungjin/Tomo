package ja.ko.tomo.feature.meeting.meeting

import androidx.annotation.StringRes
import ja.ko.tomo.domain.model.Meeting

sealed interface TomoMeetingUiState{
    data object Loading: TomoMeetingUiState
    data object Empty : TomoMeetingUiState
    data class Error(val message: String) : TomoMeetingUiState
    data class Success(
        val meeting: Meeting,
        @StringRes val buttonTextRes: Int,
        val isJoinEnabled: Boolean
    ) : TomoMeetingUiState
}