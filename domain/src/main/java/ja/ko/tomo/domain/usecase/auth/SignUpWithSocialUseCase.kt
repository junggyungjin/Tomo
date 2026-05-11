package ja.ko.tomo.domain.usecase.auth

import ja.ko.tomo.domain.model.AuthResult
import ja.ko.tomo.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpWithSocialUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(provider: String, idToken: String): AuthResult = repo.signUpWithSocial(provider, idToken)
}