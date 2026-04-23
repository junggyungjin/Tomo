package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.ChatListResult
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatRooms(): Flow<ChatListResult>
}