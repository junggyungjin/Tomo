package ja.ko.tomo.feature.meeting.meeting

sealed interface TomoMeetingEvent {
    data class ShowToast(val message: String) : TomoMeetingEvent
}