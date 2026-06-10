package com.example.repartio.data.local.dao

import androidx.room.*
import com.example.repartio.data.local.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM 'groups' ORDER BY createdAt DESC")
    fun getAllGroups(): Flow<List<GroupEntity>>  // Flow — Room emite automáticamente cuando cambia la tabla

    @Query("SELECT * FROM 'groups' WHERE id = :id")
    suspend fun getGroupById(id: Long): GroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity): Long  // devuelve el ID generado

    @Delete
    suspend fun deleteGroup(group: GroupEntity)
}