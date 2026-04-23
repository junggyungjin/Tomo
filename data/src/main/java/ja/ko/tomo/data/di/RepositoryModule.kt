package ja.ko.tomo.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ja.ko.tomo.data.repository.FakeChatRepositoryImpl
import ja.ko.tomo.data.repository.FakeMeetingRepositoryImpl
import ja.ko.tomo.data.repository.FakeUserRepositoryImpl
import ja.ko.tomo.domain.repository.ChatRepository
import ja.ko.tomo.domain.repository.MeetingRepository
import ja.ko.tomo.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        fakeMeetingRepositoryImpl: FakeMeetingRepositoryImpl
    ): MeetingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        fakeUserRepositoryImpl: FakeUserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        fakeChatRepositoryImpl: FakeChatRepositoryImpl
    ): ChatRepository
}