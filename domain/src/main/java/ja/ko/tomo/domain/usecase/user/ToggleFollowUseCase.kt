package ja.ko.tomo.domain.usecase.user

import ja.ko.tomo.domain.model.FollowResult
import ja.ko.tomo.domain.repository.UserRepository
import javax.inject.Inject

class ToggleFollowUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(userId: String): FollowResult = repo.toggleFollow(userId)
}