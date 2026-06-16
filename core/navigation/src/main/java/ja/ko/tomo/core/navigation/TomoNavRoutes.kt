package ja.ko.tomo.core.navigation

object TomoNavRoutes {
    const val AuthIntro = "auth_intro" // 로그인/회원가입 선택 화면
    const val SignUp = "sign_up"         // 회원가입 입력 화면
    const val ProfileSetUp = "profile_setup" // 프로필 설정 화면
    const val FeedList = "feed_list" // 피드 리스트 화면
    const val FeedCreate = "feed_create" // 피드 작성 화면
    const val MeetingList = "meeting_list"
    const val MeetingDetail = "meeting_detail"
    const val MeetingCreate = "meeting_create"
    const val MyPage = "my_page"
    const val ChatList = "chat_list"
    const val ChatRoom = "chat_room"

    fun profileSetupRoute(userId: String) = "$ProfileSetUp/$userId"

    fun meetingDetailRoute(meetingId: Long): String {
        return "$MeetingDetail/$meetingId"
    }

    fun chatRoomRoute(chatId: Long) = "$ChatRoom/$chatId"
}