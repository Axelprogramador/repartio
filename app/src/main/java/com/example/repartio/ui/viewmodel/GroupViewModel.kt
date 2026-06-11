package com.example.repartio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repartio.domain.model.Group
import com.example.repartio.domain.usecase.CreateGroupUseCase
import com.example.repartio.domain.usecase.DeleteGroupUseCase
import com.example.repartio.domain.usecase.GetGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Inyeccion dependencias
class GroupViewModel @Inject constructor(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase
) : ViewModel() {

    val groups: StateFlow<List<Group>> = getGroupsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createGroup(name: String) {
        viewModelScope.launch {
            createGroupUseCase(name)
        }
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            deleteGroupUseCase(group)
        }
    }
}