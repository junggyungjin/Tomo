package ja.ko.tomo.domain.usecase.chat

import ja.ko.tomo.domain.model.ChatListResult
import ja.ko.tomo.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<ChatListResult> {
        return chatRepository.getChatRooms()
    }
}