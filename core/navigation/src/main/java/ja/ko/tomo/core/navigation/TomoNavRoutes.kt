package ja.ko.tomo.core.navigation

object TomoNavRoutes {
    const val Splash = "splash"
    const val AuthIntro = "auth_intro" // 로그인/회원가입 선택 화면
    const val Login = "login"           // 실제 로그인 입력 화면
    const val SignUp = "signup"         // 회원가입 입력 화면
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