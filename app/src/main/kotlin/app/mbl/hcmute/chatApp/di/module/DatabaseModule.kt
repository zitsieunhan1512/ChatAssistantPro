package app.mbl.hcmute.chatApp.di.module

import android.content.Context
import androidx.room.Room
import app.mbl.hcmute.chatApp.data.local.room.ChatDatabase
import app.mbl.hcmute.chatApp.data.local.room.DbConstants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context, ) = Room.databaseBuilder(context, ChatDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(database: ChatDatabase) = database.chatDao()
}