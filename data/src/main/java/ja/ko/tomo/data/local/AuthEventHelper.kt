package ja.ko.tomo.data.local

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEventHelper @Inject constructor() {
    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent = _authEvent.asSharedFlow()

    suspend fun emitSessionExpired() {
        _authEvent.emit(AuthEvent.SessionExpired)
    }
}

sealed interface AuthEvent {
    data object SessionExpired : AuthEvent
}