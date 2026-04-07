package ja.ko.tomo.presentation.navigation

object TomoNavRoutes {
    const val MeetingList = "meeting_list"
    const val MeetingDetail = "meeting_detail"

    fun meetingDetailRoute(meetingId: Long): String {
        return "$MeetingDetail/$meetingId"
    }
}