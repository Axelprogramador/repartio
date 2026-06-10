package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Member
import com.example.repartio.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMembersUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    operator fun invoke(groupId: Long): Flow<List<Member>> = repository.getMembersByGroup(groupId)
}