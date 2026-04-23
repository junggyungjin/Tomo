package ja.ko.tomo.data.repository

import ja.ko.tomo.domain.model.ChatListResult
import ja.ko.tomo.domain.model.ChatRoom
import ja.ko.tomo.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeChatRepositoryImpl @Inject constructor() : ChatRepository {
    override fun getChatRooms(): Flow<ChatListResult> = flow {
        val fakeRooms = listOf(
            ChatRoom(
                id = 1L,
                meetingId = 1L,
                title = "서울 한일 언어교환 모임",
                lastMessage = "오늘 정말 즐거웠어요! 다음에 또 봐요.",
                lastMessageTime = "오후 9:30",
                unreadCount = 2
            ),
            ChatRoom(
                id = 2L,
                meetingId = 2L,
                title = "강남 일본어 스터디",
                lastMessage = "이번 주 숙제 범위가 어디인가요?",
                lastMessageTime = "어제",
                unreadCount = 0
            )
        )

        if (fakeRooms.isEmpty()) {
            emit(ChatListResult.Empty)
        }else {
            emit(ChatListResult.Success(fakeRooms))
        }
    }
}