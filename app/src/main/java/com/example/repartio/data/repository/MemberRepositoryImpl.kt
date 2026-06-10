package com.example.repartio.data.repository

import com.example.repartio.data.local.dao.MemberDao
import com.example.repartio.data.local.toDomain
import com.example.repartio.data.local.toEntity
import com.example.repartio.domain.model.Member
import com.example.repartio.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val dao: MemberDao
) : MemberRepository {

    override fun getMembersByGroup(groupId: Long): Flow<List<Member>> =
        dao.getMembersByGroup(groupId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertMember(member: Member): Long =
        dao.insertMember(member.toEntity())

    override suspend fun deleteMember(member: Member) =
        dao.deleteMember(member.toEntity())
}