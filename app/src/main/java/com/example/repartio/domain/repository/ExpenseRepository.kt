package com.example.repartio.domain.repository

import com.example.repartio.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpensesByGroup(groupId: Long): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense): Long
    suspend fun deleteExpense(expense: Expense)
}