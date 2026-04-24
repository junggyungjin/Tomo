package ja.ko.tomo.core.navigation

object TomoNavRoutes {
    const val MeetingList = "meeting_list"
    const val MeetingDetail = "meeting_detail"
    const val MeetingCreate = "meeting_create"
    const val MyPage = "my_page"
    const val ChatList = "chat_list"
    const val ChatRoom = "chat_room"

    fun meetingDetailRoute(meetingId: Long): String {
        return "$MeetingDetail/$meetingId"
    }

    fun chatRoomRoute(chatId: Long) = "$ChatRoom/$chatId"
}