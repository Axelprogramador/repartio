package com.example.repartio.domain.model

data class Group(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)