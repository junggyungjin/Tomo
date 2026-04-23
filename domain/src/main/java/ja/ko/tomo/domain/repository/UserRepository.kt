package ja.ko.tomo.domain.repository

import ja.ko.tomo.domain.model.UserResult

interface UserRepository {
    suspend fun getMyInfo(): UserResult
}