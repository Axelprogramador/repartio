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
            .addMigrations(MIGRATION_2_3)
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

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE INDEX IF NOT EXISTS index_members_groupId ON members(groupId)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_expenses_groupId ON expenses(groupId)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_expenses_payerId ON expenses(payerId)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_expense_participants_expenseId ON expense_participants(expenseId)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_expense_participants_memberId ON expense_participants(memberId)")
    }
}