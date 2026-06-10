package com.example.repartio.data.local.dao

import androidx.room.*
import com.example.repartio.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members WHERE groupId = :groupId")
    fun getMembersByGroup(groupId: Long): Flow<List<MemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Delete
    suspend fun deleteMember(member: MemberEntity)
}