package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.ChatListResult
import ja.ko.tomo.domain.model.ChatMessageListResult
import ja.ko.tomo.domain.model.ChatMessageResult
import ja.ko.tomo.domain.model.MeetingResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    // 참여 중인 채팅방 목록(실시간)
    fun getChatRooms(): Flow<ChatListResult>
    // 특정 채팅방의 메시지 목록 (실시간)
    fun getMessages(roomId: Long): Flow<ChatMessageListResult>

    suspend fun sendMessage(roomId: Long, text: String): ChatMessageResult
}