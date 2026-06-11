package com.example.repartio.data.local

import com.example.repartio.data.local.entity.ExpenseEntity
import com.example.repartio.data.local.entity.ExpenseParticipantEntity
import com.example.repartio.data.local.entity.GroupEntity
import com.example.repartio.data.local.entity.MemberEntity
import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.model.ExpenseParticipant
import com.example.repartio.domain.model.Group
import com.example.repartio.domain.model.Member

// Para convertir entre capas

fun GroupEntity.toDomain() = Group(id = id, name = name, createdAt = createdAt)
fun Group.toEntity() = GroupEntity(id = id, name = name, createdAt = createdAt)

fun MemberEntity.toDomain() = Member(id = id, groupId = groupId, name = name)
fun Member.toEntity() = MemberEntity(id = id, groupId = groupId, name = name)

fun ExpenseEntity.toDomain() = Expense(id = id, groupId = groupId, payerId = payerId, description = description, amount = amount, createdAt = createdAt)
fun Expense.toEntity() = ExpenseEntity(id = id, groupId = groupId, payerId = payerId, description = description, amount = amount, createdAt = createdAt)

fun ExpenseParticipantEntity.toDomain() = ExpenseParticipant(
    id = id,
    expenseId = expenseId,
    memberId = memberId,
    amountOwed = amountOwed
)

fun ExpenseParticipant.toEntity() = ExpenseParticipantEntity(
    id = id,
    expenseId = expenseId,
    memberId = memberId,
    amountOwed = amountOwed
)