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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.repartio.data.local.dao.ExpenseParticipantDao

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
        )
            .fallbackToDestructiveMigration()  // Para desarrollo, borra y recrea
            .build()

    @Provides
    fun provideGroupDao(db: RepartioDatabase): GroupDao = db.groupDao()

    @Provides
    fun provideMemberDao(db: RepartioDatabase): MemberDao = db.memberDao()

    @Provides
    fun provideExpenseDao(db: RepartioDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideExpenseParticipantDao(db: RepartioDatabase): ExpenseParticipantDao = db.expenseParticipantDao()
}

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS expense_participants (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                expenseId INTEGER NOT NULL,
                memberId INTEGER NOT NULL,
                amountOwed REAL NOT NULL,
                FOREIGN KEY (expenseId) REFERENCES expenses(id) ON DELETE CASCADE,
                FOREIGN KEY (memberId) REFERENCES members(id) ON DELETE CASCADE
            )
        """.trimIndent()
        )
    }
}