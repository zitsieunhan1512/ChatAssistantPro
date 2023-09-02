package app.mbl.hcmute.chatApp.di.module

import app.mbl.hcmute.chatApp.data.repository.ChatRepository
import app.mbl.hcmute.chatApp.data.repository.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindProductRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

}