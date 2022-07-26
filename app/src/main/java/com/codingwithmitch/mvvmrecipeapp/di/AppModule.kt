package com.codingwithmitch.mvvmrecipeapp.di

import android.content.Context
import com.codingwithmitch.mvvmrecipeapp.presentation.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Singleton
    @Provides
    fun provideRandomString(): String {
        return "Hey look a random String!!!"
    }
}