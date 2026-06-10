package com.example.repartio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.repartio.data.local.dao.ExpenseDao
import com.example.repartio.data.local.dao.GroupDao
import com.example.repartio.data.local.dao.MemberDao
import com.example.repartio.data.local.entity.ExpenseEntity
import com.example.repartio.data.local.entity.GroupEntity
import com.example.repartio.data.local.entity.MemberEntity

@Database(
    entities = [GroupEntity::class, MemberEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false    // dejo en false mientras hago la app, en produccion real cambio a true
)
abstract class RepartioDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun memberDao(): MemberDao
    abstract fun expenseDao(): ExpenseDao
}