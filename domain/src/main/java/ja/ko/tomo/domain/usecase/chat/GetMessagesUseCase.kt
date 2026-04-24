package ja.ko.tomo.domain.usecase.chat

import ja.ko.tomo.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val repo: ChatRepository) {
    operator fun invoke(roomId: Long) = repo.getMessages(roomId = roomId)
}