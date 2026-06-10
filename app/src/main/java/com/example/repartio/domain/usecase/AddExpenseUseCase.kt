package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(groupId: Long, payerId: Long, description: String, amount: Double): Long {
        require(description.isNotBlank()) { "Description cannot be empty" }
        require(amount > 0) { "Amount must be positive" }
        return repository.insertExpense(
            Expense(groupId = groupId, payerId = payerId, description = description.trim(), amount = amount)
        )
    }
}