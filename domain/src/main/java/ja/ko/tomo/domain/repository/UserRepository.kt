package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.User

interface UserRepository {
    suspend fun getMyInfo(): User?
}