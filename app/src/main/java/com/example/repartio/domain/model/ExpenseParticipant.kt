package com.example.repartio.domain.model

data class ExpenseParticipant(
    val id: Long = 0,
    val expenseId: Long,
    val memberId: Long,
    val amountOwed: Double  // cantidad exacta que debe este miembro de este gasto
)