package ja.ko.tomo.presentation.meeting

sealed interface TomoMeetingEvent {
    data class ShowToast(val message: String) : TomoMeetingEvent
}