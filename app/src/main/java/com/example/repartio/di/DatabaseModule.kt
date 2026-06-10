package com.example.repartio.di

import android.content.Context
import androidx.room.Room
import com.example.repartio.data.local.RepartioDatabase
import com.example.repartio.data.local.dao.ExpenseDao
import com.example.repartio.data.local.dao.GroupDao
import com.example.repartio.data.local.dao.MemberDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RepartioDatabase =
        Room.databaseBuilder(
            context,
            RepartioDatabase::class.java,
            "repartio.db"
        ).build()

    @Provides
    fun provideGroupDao(db: RepartioDatabase): GroupDao = db.groupDao()

    @Provides
    fun provideMemberDao(db: RepartioDatabase): MemberDao = db.memberDao()

    @Provides
    fun provideExpenseDao(db: RepartioDatabase): ExpenseDao = db.expenseDao()
}