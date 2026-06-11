package com.example.repartio.data.local.dao

import androidx.room.*
import com.example.repartio.data.local.entity.ExpenseParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseParticipantDao {

    @Query("SELECT * FROM expense_participants WHERE expenseId = :expenseId")
    fun getParticipantsByExpense(expenseId: Long): Flow<List<ExpenseParticipantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ExpenseParticipantEntity>)

    @Query("DELETE FROM expense_participants WHERE expenseId = :expenseId")
    suspend fun deleteParticipantsByExpense(expenseId: Long)
}