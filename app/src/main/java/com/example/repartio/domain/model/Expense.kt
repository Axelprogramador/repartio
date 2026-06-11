package com.example.repartio.domain.model

data class Expense(
    val id: Long = 0,
    val groupId: Long,
    val payerId: Long,
    val description: String,
    val amount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val participants: List<ExpenseParticipant> = emptyList()
)