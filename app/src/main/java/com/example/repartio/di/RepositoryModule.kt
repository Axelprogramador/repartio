package com.example.repartio.di

import com.example.repartio.data.repository.ExpenseRepositoryImpl
import com.example.repartio.data.repository.GroupRepositoryImpl
import com.example.repartio.data.repository.MemberRepositoryImpl
import com.example.repartio.domain.repository.ExpenseRepository
import com.example.repartio.domain.repository.GroupRepository
import com.example.repartio.domain.repository.MemberRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Lo mismo que @Bean con Spring
    @Binds
    @Singleton
    abstract fun bindGroupRepository(impl: GroupRepositoryImpl): GroupRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(impl: MemberRepositoryImpl): MemberRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository
}