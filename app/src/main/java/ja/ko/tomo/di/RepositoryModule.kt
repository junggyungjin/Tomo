package ja.ko.tomo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ja.ko.tomo.data.repository.FakeMeetingRepositoryImpl
import ja.ko.tomo.domain.repository.MeetingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        fakeMeetingRepositoryImpl: FakeMeetingRepositoryImpl
    ): MeetingRepository
}