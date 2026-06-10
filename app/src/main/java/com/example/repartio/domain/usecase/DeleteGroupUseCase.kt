package com.example.repartio.domain.usecase


import com.example.repartio.domain.model.Group
import com.example.repartio.domain.repository.GroupRepository
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(group: Group) = repository.deleteGroup(group)
}