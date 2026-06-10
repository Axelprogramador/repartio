package com.example.repartio.data.repository

import com.example.repartio.data.local.dao.ExpenseDao
import com.example.repartio.data.local.toDomain
import com.example.repartio.data.local.toEntity
import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun getExpensesByGroup(groupId: Long): Flow<List<Expense>> =
        dao.getExpensesByGroup(groupId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertExpense(expense: Expense): Long =
        dao.insertExpense(expense.toEntity())

    override suspend fun deleteExpense(expense: Expense) =
        dao.deleteExpense(expense.toEntity())
}