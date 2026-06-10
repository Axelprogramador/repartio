package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Member
import com.example.repartio.domain.repository.MemberRepository
import javax.inject.Inject

class AddMemberUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(groupId: Long, name: String): Long {
        require(name.isNotBlank()) { "Member name cannot be empty" }
        return repository.insertMember(Member(groupId = groupId, name = name.trim()))
    }
}