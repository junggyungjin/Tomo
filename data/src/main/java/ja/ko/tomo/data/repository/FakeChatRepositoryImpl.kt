package ja.ko.tomo.data.repository

import ja.ko.tomo.domain.model.ChatListResult
import ja.ko.tomo.domain.model.ChatMessage
import ja.ko.tomo.domain.model.ChatMessageListResult
import ja.ko.tomo.domain.model.ChatMessageResult
import ja.ko.tomo.domain.model.ChatRoom
import ja.ko.tomo.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FakeChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val fakeMessages = listOf(
        ChatMessage(
            id = 1L,
            senderNickname = "토모 매니저",
            message = "안녕하세요! 모임에 참여해주셔서 감사합니다.",
            sentTime = "오후 2:00",
            isMine = false
        ),
        ChatMessage(
            id = 2L,
            senderNickname = "토모 매니저",
            message = "모임 장소는 홍대입구역 1번 출구 앞입니다.",
            sentTime = "오후 2:05",
            isMine = false
        ),
        ChatMessage(
            id = 3L,
            senderNickname = "나",
            message = "네! 확인했습니다. 제 시간에 맞춰 갈게요.",
            sentTime = "오후 2:10",
            isMine = true
        )
    )

    // 실시간 메시지 흐름을 관리할 가짜 DB
    private val _messages = MutableStateFlow<List<ChatMessage>>(fakeMessages)

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

    // 메시지 목록 가져오기
    override fun getMessages(roomId: Long): Flow<ChatMessageListResult> {
        return _messages.map { list ->
            if (list.isEmpty()) ChatMessageListResult.Empty
            else ChatMessageListResult.Success(list)
        }
    }

    // 메시지 보내기
    override suspend fun sendMessage(
        roomId: Long,
        text: String
    ): ChatMessageResult {
        delay(300)

        val currentTime = SimpleDateFormat("a hh:mm", Locale.KOREAN)
            .format(Date())

        val newMessage = ChatMessage(
            id = System.currentTimeMillis(),
            senderNickname = "나",
            message = text,
            sentTime = currentTime,
            isMine = true
        )

        // 리스트에 새 메시지 추가 -> 자동으로 getMessage의 Flow가 발행됨
        _messages.value = _messages.value + newMessage

        return ChatMessageResult.Success(newMessage)
    }
}