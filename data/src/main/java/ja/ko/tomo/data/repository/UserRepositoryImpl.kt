package ja.ko.tomo.data.repository

import ja.ko.tomo.data.dto.request.UpdateUserProfileRequestDto
import ja.ko.tomo.data.mapper.toDomain
import ja.ko.tomo.data.remote.UserApiService
import ja.ko.tomo.domain.model.Gender
import ja.ko.tomo.domain.model.UserResult
import ja.ko.tomo.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
): UserRepository {
    // 내 정보 조회
    override suspend fun getMyInfo(): UserResult {
        return try {
            val response = userApiService.getMyInfo()

            if (response.success && response.data != null) {
                UserResult.Success(response.data.toDomain())
            } else {
                val errorBody = response.error
                Timber.tag("UserRepo").e("내 정보 조회 실패: code=${errorBody?.code}, message=${errorBody?.message}")
                UserResult.Error(response.error?.message ?: "내 정보를 가져오는데 실패했습니다")
            }

        }catch (e: Exception) {
            Timber.tag("UserRepo").e(e, "내 정보 조회 중 예외 발생")
            UserResult.Error(e.message ?: "알 수 업슨ㄴ 에러가 발생했습니다.")
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        nickname: String,
        nationality: String,
        gender: Gender,
        profileImageUrl: String?
    ): UserResult {
        return try {
            val response = userApiService.updateUserProfile(
                UpdateUserProfileRequestDto(
                    userId = userId,
                    nickname = nickname,
                    nationality = nationality,
                    gender = gender.name,
                    profileImageUrl = profileImageUrl
                )
            )

            if (response.success && response.data != null) {
                UserResult.Success(response.data.toDomain())
            } else {
                val errorBody = response.error
                Timber.tag("UserRepo").e("API 실패: code=${errorBody?.code}, message=${errorBody?.message}")
                UserResult.Error(response.error?.message ?: "서버 응답 오류가 발생했습니다.")
            }

        }catch (e: Exception) {
            Timber.tag("UserRepo").e(e, "프로필 업데이트 중 예상치 못한 예외 발생")
            UserResult.Error(e.message ?: "프로필 업데이트 중 알 수 없는 에러가 발생했습니다.")
        }
    }
}