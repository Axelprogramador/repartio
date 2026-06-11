package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.model.ExpenseParticipant
import com.example.repartio.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(
        groupId: Long,
        payerId: Long,
        description: String,
        amount: Double,
        participants: List<Pair<Long, Double>>
    ): Long {
        require(description.isNotBlank()) { "Description cannot be empty" }
        require(amount > 0) { "Amount must be positive" }

        if (participants.isNotEmpty()) {
            val totalParticipants = participants.sumOf { it.second }
            require(Math.abs(totalParticipants - amount) < 0.01) {
                "Participants amounts must sum to total expense amount"
            }
        }

        val expense = Expense(
            groupId = groupId,
            payerId = payerId,
            description = description.trim(),
            amount = amount,
            participants = participants.map { (memberId, amountOwed) ->
                ExpenseParticipant(expenseId = 0, memberId = memberId, amountOwed = amountOwed)
            }
        )
        return repository.insertExpense(expense)
    }
}