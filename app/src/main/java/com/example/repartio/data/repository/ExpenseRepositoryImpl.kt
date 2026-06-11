package com.example.repartio.data.repository

import com.example.repartio.data.local.dao.ExpenseDao
import com.example.repartio.data.local.dao.ExpenseParticipantDao
import com.example.repartio.data.local.toDomain
import com.example.repartio.data.local.toEntity
import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val participantDao: ExpenseParticipantDao
) : ExpenseRepository {

    override fun getExpensesByGroup(groupId: Long): Flow<List<Expense>> =
        expenseDao.getExpensesByGroup(groupId).flatMapLatest { list ->
            if (list.isEmpty()) {
                flowOf(emptyList())
            } else {
                // Cargamos participantes para cada gasto en paralelo
                combine(list.map { expenseEntity ->
                    participantDao.getParticipantsByExpense(expenseEntity.id)
                        .map { participants ->
                            expenseEntity.toDomain().copy(
                                participants = participants.map { it.toDomain() }
                            )
                        }
                }) { it.toList() }
            }
        }

    override suspend fun insertExpense(expense: Expense): Long {
        val expenseId = expenseDao.insertExpense(expense.toEntity())
        val participants = expense.participants.map {
            it.copy(expenseId = expenseId).toEntity()
        }
        if (participants.isNotEmpty()) {
            participantDao.insertParticipants(participants)
        }
        return expenseId
    }

    override suspend fun deleteExpense(expense: Expense) =
        expenseDao.deleteExpense(expense.toEntity())  // En cascada para borrar automaticamente participatnes cuando borro gasto
}