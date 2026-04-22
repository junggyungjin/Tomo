package ja.ko.tomo.domain.usecase.user

import ja.ko.tomo.domain.model.User
import ja.ko.tomo.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? {
        return userRepository.getMyInfo()
    }
}