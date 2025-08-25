package com.iaguapacha.reminder.di

import android.content.Context
import androidx.room.Room
import com.iaguapacha.reminder.data.AppDatabase
import com.iaguapacha.reminder.app.NotificationScheduler
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
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "birthday.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideReminderDao(database: AppDatabase) = database.reminderDao()

    @Provides
    @Singleton
    fun provideNotificationScheduler(@ApplicationContext context: Context): NotificationScheduler {
        return NotificationScheduler(context)
    }
}
