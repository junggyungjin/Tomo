package ja.ko.tomo.presentation.meetingcreate

sealed interface MeetingCreateUiEffect {
    data class ShowToast(val message: String): MeetingCreateUiEffect
    data object NavigateBack : MeetingCreateUiEffect
    data class ShowSnackbar(val message: String): MeetingCreateUiEffect
}