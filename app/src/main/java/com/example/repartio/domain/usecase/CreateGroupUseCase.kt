package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Group
import com.example.repartio.domain.repository.GroupRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(name: String): Long {
        require(name.isNotBlank()) { "Group name cannot be empty" }
        return repository.insertGroup(Group(name = name.trim()))
    }
}