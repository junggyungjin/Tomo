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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

// 이렇게 하면 앱이 켜져있는동안 이 클래스의 인스턴스를 딱 하나만 만들어서 돌려씀
// 장점 1. 데이터 일치 - 1번방에서 메시지를 보냈을때 목록 화면에 즉시 반영되는 이유
// 장점 2. 메모리 절약 - 화면마다 새로 만들지 않으니 메모리를 아낄 수 있음
@Singleton
class FakeChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val initialMessages = listOf(
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
        )
    )

    // 방 번호를 키로 하여 각각의 메시지 리스트를 관리하는 보관함
    private val roomMessagesMap = mutableMapOf<Long, MutableStateFlow<List<ChatMessage>>>()
    // 방별 안 읽은 메시지 개수 관리
    // 실제 다른 메신저앱들은 서버-로컬db-ui 3박자가 맞물려 돌아감
    private val roomUnreadCountMap = MutableStateFlow<Map<Long, Int>>(mapOf(1L to 2))
    // 모든 메시지 변화를 통합 관리할 신호기
    private val allMessagesSignal = MutableStateFlow(0)

    // 방 번호에 해당하는 Flow를 가져오거나 없으면 새로 만드는 도우미 함수
    private fun getOrCreateMessagesFlow(roomId: Long): MutableStateFlow<List<ChatMessage>> {
        return roomMessagesMap.getOrPut(roomId) {
            val initial = if (roomId == 1L) initialMessages else emptyList()
            MutableStateFlow(initial)
        }
    }

    override fun getChatRooms(): Flow<ChatListResult> = combine(
        roomUnreadCountMap,
        allMessagesSignal
    ) { unreadMap, _ ->
        // conbine 인자중에 하나라도 값이 바뀌면(업데이트되면) combine 블록 내부의 코드가 즉시 다시 실행됨
        val rooms = listOf(
            buildChatRoom(
                id = 1L,
                meetingId = 1L,
                title = "서울 한일 언어교환 모임",
                unreadCount = unreadMap[1L] ?: 0
            ),
            buildChatRoom(
                id = 2L,
                meetingId = 2L,
                title = "강남 일본어 스터디",
                unreadCount = unreadMap[2L] ?: 0
            )
        )
        ChatListResult.Success(rooms)
    }

    // 각 방의 최신 상태를 반영하여 ChatRoom 객체를 생성하는 헬퍼 함수
    private fun buildChatRoom(id: Long, meetingId: Long, title: String, unreadCount: Int): ChatRoom {
        val messages = getOrCreateMessagesFlow(id).value
        val lastMsg = messages.lastOrNull()

        return ChatRoom(
            id = id,
            meetingId = meetingId,
            title = title,
            lastMessage = lastMsg?.message ?: "대화 내용이 없습니다",
            lastMessageTime = lastMsg?.sentTime ?: "",
            unreadCount = unreadCount // 1번방이고 초기 상태일태만 안읽은 메시지를 보여주기 위함
        )
    }

    // 메시지 목록 가져오기
    override fun getMessages(roomId: Long): Flow<ChatMessageListResult> {
        return getOrCreateMessagesFlow(roomId).map { list ->
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

        // 메시지 저장 로직
        // 이렇게 하면 flow.value = flow.value + new 방식보다 원자성과 스레드 안정성에서 이점이 있음
        getOrCreateMessagesFlow(roomId = roomId).update { it + newMessage }

        // 목록 갱신을 위해 unreadCount를 건드려 신호를 줌 (실제론 서버가 할 일)
        // combine이 이를 감지하고 getChatRooms를 다시 실행하게 됨
        allMessagesSignal.update { it + 1 }

        return ChatMessageResult.Success(newMessage)
    }

    override suspend fun markAsRead(roomId: Long) {
        roomUnreadCountMap.update { currentMap ->
            val newMap = currentMap.toMutableMap()
            newMap[roomId] = 0
            newMap
        }
        // 그냥 읽었다는 신호를 주기 위함. it +1은 중요하지않음
        allMessagesSignal.update { it + 1 }
    }
}