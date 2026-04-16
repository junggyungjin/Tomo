package ja.ko.tomo.feature.meeting.meetingcreate

sealed interface MeetingCreateUiEffect {
    data class ShowToast(val message: String): MeetingCreateUiEffect
    data object NavigateBack : MeetingCreateUiEffect
    data class ShowSnackbar(val message: String): MeetingCreateUiEffect
}