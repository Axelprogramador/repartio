package com.example.repartio.domain.repository

import com.example.repartio.domain.model.Member
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun getMembersByGroup(groupId: Long): Flow<List<Member>>
    suspend fun insertMember(member: Member): Long
    suspend fun deleteMember(member: Member)
}