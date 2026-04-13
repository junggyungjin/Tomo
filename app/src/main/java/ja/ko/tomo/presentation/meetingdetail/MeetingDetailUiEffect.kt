package ja.ko.tomo.presentation.meetingdetail

sealed interface MeetingDetailUiEffect {
    data class ShowToast(val message: String) : MeetingDetailUiEffect
    data object NavigateBack : MeetingDetailUiEffect
    data class ShowSnackbar(val message : String) : MeetingDetailUiEffect
}