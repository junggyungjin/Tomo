package ja.ko.tomo.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    // 현재 로그인한 유저 ID Flow
    val userId: Flow<String?> = dataStore.data.map { it[USER_ID_KEY] }

    // 세션 정보 저장
    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // 로그아웃시 세션 삭제
    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}