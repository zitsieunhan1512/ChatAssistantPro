package app.mbl.hcmute.chatApp.di.module.openAiModule

import com.aallam.openai.client.OpenAI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class OpenAiModule {

    @Provides
    @Singleton
    fun openAi(): OpenAI = OpenAI(OpenAiConst.OPENAI_API.api_key)
}