package com.example.repartio.data.repository

import com.example.repartio.data.local.dao.GroupDao
import com.example.repartio.data.local.toDomain
import com.example.repartio.data.local.toEntity
import com.example.repartio.domain.model.Group
import com.example.repartio.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val dao: GroupDao
) : GroupRepository {

    override fun getAllGroups(): Flow<List<Group>> =
        dao.getAllGroups().map { list -> list.map { it.toDomain() } }

    override suspend fun getGroupById(id: Long): Group? =
        dao.getGroupById(id)?.toDomain()

    override suspend fun insertGroup(group: Group): Long =
        dao.insertGroup(group.toEntity())

    override suspend fun deleteGroup(group: Group) =
        dao.deleteGroup(group.toEntity())
}