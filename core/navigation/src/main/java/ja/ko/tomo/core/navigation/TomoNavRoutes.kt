package ja.ko.tomo.core.navigation

object TomoNavRoutes {
    const val MeetingList = "meeting_list"
    const val MeetingDetail = "meeting_detail"

    const val MeetingCreate = "meeting_create"

    fun meetingDetailRoute(meetingId: Long): String {
        return "$MeetingDetail/$meetingId"
    }
}