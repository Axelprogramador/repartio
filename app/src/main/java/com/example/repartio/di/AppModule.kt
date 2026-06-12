package com.example.repartio.di

import android.content.Context
import com.example.repartio.ui.theme.LanguageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLanguageManager(
        @ApplicationContext context: Context
    ): LanguageManager = LanguageManager(context)
}