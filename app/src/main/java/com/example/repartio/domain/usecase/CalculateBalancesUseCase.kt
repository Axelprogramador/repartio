package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.model.Member
import javax.inject.Inject

// Representa una deuda: "from" le debe "amount" a "to"
data class Settlement(
    val fromMemberId: Long,
    val fromMemberName: String,
    val toMemberId: Long,
    val toMemberName: String,
    val amount: Double
)

class CalculateBalancesUseCase @Inject constructor() {

    operator fun invoke(members: List<Member>, expenses: List<Expense>): List<Settlement> {
        if (members.isEmpty() || expenses.isEmpty()) return emptyList()

        val memberMap = members.associateBy { it.id }
        val totalExpenses = expenses.sumOf { it.amount }
        val sharePerPerson = totalExpenses / members.size

        // Calcula el balance neto de cada miembro: positivo = le deben, negativo = debe
        val balances = members.associate { it.id to 0.0 }.toMutableMap()
        expenses.forEach { expense ->
            balances[expense.payerId] = (balances[expense.payerId] ?: 0.0) + expense.amount
        }
        balances.keys.forEach { id ->
            balances[id] = (balances[id] ?: 0.0) - sharePerPerson
        }

        // Empareja deudores con quienes pagaron de más
        val debtors = balances.filter { it.value < -0.01 }
            .map { it.key to it.value }.toMutableList()
        val creditors = balances.filter { it.value > 0.01 }
            .map { it.key to it.value }.toMutableList()

        val settlements = mutableListOf<Settlement>()

        //bucle con el que emparejo deudores con acreedores uno a uno, reduce numero de trasferencias
        var i = 0; var j = 0
        while (i < debtors.size && j < creditors.size) {
            val (debtorId, debtorBalance) = debtors[i]
            val (creditorId, creditorBalance) = creditors[j]
            val amount = minOf(-debtorBalance, creditorBalance)

            settlements.add(
                Settlement(
                    fromMemberId = debtorId,
                    fromMemberName = memberMap[debtorId]?.name ?: "",
                    toMemberId = creditorId,
                    toMemberName = memberMap[creditorId]?.name ?: "",
                    amount = Math.round(amount * 100) / 100.0
                )
            )

            debtors[i] = debtorId to (debtorBalance + amount)
            creditors[j] = creditorId to (creditorBalance - amount)

            if (-debtors[i].second < 0.01) i++
            if (creditors[j].second < 0.01) j++
        }
        //
        return settlements
    }
}