package ja.ko.tomo.domain.model

data class Meeting(
    val id: Long,
    val title: String,
    val subtitle: String,
    val dateTime: String,
    val location: String,
    val isClosed: Boolean,
    val isJoined: Boolean,
    val isFavorite: Boolean,
    val capacity: Int // 정원 정보
)