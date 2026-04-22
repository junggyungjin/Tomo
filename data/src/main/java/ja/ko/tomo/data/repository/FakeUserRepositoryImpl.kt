package ja.ko.tomo.data.repository

import ja.ko.tomo.domain.model.User
import ja.ko.tomo.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeUserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getMyInfo(): User? {
        delay(500)
        return User(
            id = 1L,
            email = "tomo@gmail.com",
            nickname = "토모 개발자",
            profileImageUrl = null,
            introduction = "요로시쿠!"
        )
    }

}