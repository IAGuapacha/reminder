package com.iaguapacha.reminder.di

import android.content.Context
import com.iaguapacha.reminder.data.datasource.DeviceContactsSource
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
    fun provideDeviceContactsSource(@ApplicationContext context: Context): DeviceContactsSource {
        return DeviceContactsSource(context)
    }
}