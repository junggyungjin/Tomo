package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.FollowResult
import ja.ko.tomo.domain.model.Gender
import ja.ko.tomo.domain.model.UserResult

interface UserRepository {
    suspend fun getMyInfo(): UserResult

    suspend fun updateUserProfile(
        userId: String,
        nickname: String,
        nationality: String,
        gender: Gender,
        profileImageUrl: String?
    ): UserResult

    suspend fun toggleFollow(userId: String): FollowResult
}