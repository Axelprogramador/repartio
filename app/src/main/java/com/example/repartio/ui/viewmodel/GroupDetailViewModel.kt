package com.example.repartio.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repartio.domain.model.Expense
import com.example.repartio.domain.model.Member
import com.example.repartio.domain.repository.GroupRepository
import com.example.repartio.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val getMembersUseCase: GetMembersUseCase,
    private val addMemberUseCase: AddMemberUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val calculateBalancesUseCase: CalculateBalancesUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val groupRepository: GroupRepository,  // nuevo
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val groupId: Long = checkNotNull(savedStateHandle["groupId"])

    val members: StateFlow<List<Member>> = getMembersUseCase(groupId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<Expense>> = getExpensesUseCase(groupId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settlements = members.combine(expenses) { memberList, expenseList ->
        calculateBalancesUseCase(memberList, expenseList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val group = kotlinx.coroutines.flow.flow {
        groupRepository.getGroupById(groupId)?.let { emit(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun addMember(name: String) {
        viewModelScope.launch {
            addMemberUseCase(groupId, name)
        }
    }
    
    fun addExpense(
        payerId: Long,
        description: String,
        amount: Double,
        participants: List<Pair<Long, Double>>
    ) {
        viewModelScope.launch {
            addExpenseUseCase(groupId, payerId, description, amount, participants)
        }
    }
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            deleteExpenseUseCase(expense)
        }
    }
}