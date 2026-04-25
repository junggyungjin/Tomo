package ja.ko.tomo.domain.usecase.chat

import ja.ko.tomo.domain.repository.ChatRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val repo: ChatRepository
) {
    suspend operator fun invoke(roomId: Long) {
        repo.markAsRead(roomId = roomId)
    }
}