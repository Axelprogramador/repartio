package com.example.repartio.domain.repository

import com.example.repartio.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getAllGroups(): Flow<List<Group>>  // Uso flow para que se actualice automaticamente datos si la BD cambia
    suspend fun getGroupById(id: Long): Group? // Uso suspend porque es el equivalente a Async
    suspend fun insertGroup(group: Group): Long
    suspend fun deleteGroup(group: Group)
}