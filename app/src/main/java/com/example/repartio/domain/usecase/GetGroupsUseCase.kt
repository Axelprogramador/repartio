package com.example.repartio.domain.usecase

import com.example.repartio.domain.model.Group
import com.example.repartio.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    operator fun invoke(): Flow<List<Group>> = repository.getAllGroups()
}