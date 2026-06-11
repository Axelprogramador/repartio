package com.example.repartio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.repartio.domain.model.Member
import com.example.repartio.domain.usecase.Settlement
import com.example.repartio.ui.viewmodel.GroupDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    onBack: () -> Unit,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val members by viewModel.members.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val settlements by viewModel.settlements.collectAsState()

    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Group") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                SmallFloatingActionButton(onClick = { showAddMemberDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add member")
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(onClick = { showAddExpenseDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add expense")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Sección miembros
            item {
                Text("Members", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp))
            }
            items(members) { member ->
                Text("• ${member.name}", modifier = Modifier.padding(vertical = 4.dp))
            }

            // Sección gastos
            item {
                Text("Expenses", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp))
            }
            if (expenses.isEmpty()) {
                item { Text("No expenses yet", style = MaterialTheme.typography.bodyMedium) }
            } else {
                items(expenses) { expense ->
                    val payerName = members.find { it.id == expense.payerId }?.name ?: ""
                    ListItem(
                        headlineContent = { Text(expense.description) },
                        supportingContent = { Text("Paid by $payerName") },
                        trailingContent = { Text("%.2f€".format(expense.amount)) }
                    )
                    HorizontalDivider()
                }
            }

            // Sección balances
            item {
                Text("Settlements", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp))
            }
            if (settlements.isEmpty()) {
                item { Text("Everyone is even!", style = MaterialTheme.typography.bodyMedium) }
            } else {
                items(settlements) { settlement ->
                    SettlementItem(settlement)
                }
            }
        }
    }

    if (showAddMemberDialog) {
        AddMemberDialog(
            onConfirm = { name ->
                viewModel.addMember(name)
                showAddMemberDialog = false
            },
            onDismiss = { showAddMemberDialog = false }
        )
    }

    if (showAddExpenseDialog) {
        AddExpenseDialog(
            members = members,
            onConfirm = { payerId, description, amount ->
                viewModel.addExpense(payerId, description, amount)
                showAddExpenseDialog = false
            },
            onDismiss = { showAddExpenseDialog = false }
        )
    }
}

@Composable
private fun SettlementItem(settlement: com.example.repartio.domain.usecase.Settlement) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
    ) {
        Text(
            text = "${settlement.fromMemberName} → ${settlement.toMemberName}: %.2f€".format(settlement.amount),
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun AddMemberDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add member") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onConfirm(name) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseDialog(
    members: List<Member>,
    onConfirm: (Long, String, Double) -> Unit,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedMember by remember { mutableStateOf(members.firstOrNull()) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add expense") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    singleLine = true
                )
                // Dropdown para seleccionar quién pagó
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedMember?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Paid by") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.name) },
                                onClick = {
                                    selectedMember = member
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amountDouble = amount.toDoubleOrNull()
                if (description.isNotBlank() && amountDouble != null && selectedMember != null) {
                    onConfirm(selectedMember!!.id, description, amountDouble)
                }
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}