package ja.ko.tomo.data.repository

import ja.ko.tomo.domain.model.FollowResult
import ja.ko.tomo.domain.model.Gender
import ja.ko.tomo.domain.model.User
import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeUserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getMyInfo(): UserResult {
        delay(500)
        val user = User(
            id = "1",
            email = "tomo@gmail.com",
            nickname = "토모 개발자",
            handle = "",
            nationality = "",
            profileImageUrl = null,
            status = "ACTIVE",
            introduction = "요로시쿠!"
        )
        return UserResult.Success(user = user)
    }

    override suspend fun updateUserProfile(
        userId: String,
        nickname: String,
        nationality: String,
        gender: Gender,
        profileImageUrl: String?
    ): UserResult {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFollow(userId: String): FollowResult {
        TODO("Not yet implemented")
    }

}