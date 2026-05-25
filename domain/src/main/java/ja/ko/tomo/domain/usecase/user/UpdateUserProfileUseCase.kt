package ja.ko.tomo.domain.usecase.user

import ja.ko.tomo.domain.model.Gender
import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repo:  UserRepository
) {
    suspend operator fun invoke(
        userId: String,
        nickname: String,
        nationality: String,
        gender: Gender,
        profileImageUrl: String?
    ): UserResult = repo.updateUserProfile(userId, nickname, nationality, gender, profileImageUrl)
}