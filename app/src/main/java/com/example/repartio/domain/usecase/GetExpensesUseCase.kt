package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(groupId: Long): Flow<List<Expense>> = repository.getExpensesByGroup(groupId)
}