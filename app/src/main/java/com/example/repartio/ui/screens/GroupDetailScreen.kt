package com.example.repartio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.repartio.domain.model.Member
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
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showAddMemberDialog = true },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    text = { Text("Add member") },
                )
                ExtendedFloatingActionButton(
                    onClick = { showAddExpenseDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add expense") },
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text("Members", style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp))
            }
            items(members) { member ->
                Text("• ${member.name}", modifier = Modifier.padding(vertical = 4.dp))
            }

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
            onConfirm = { payerId, description, amount, participants ->
                viewModel.addExpense(payerId, description, amount, participants)
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
    onConfirm: (Long, String, Double, List<Pair<Long, Double>>) -> Unit,
    onDismiss: () -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedPayer by remember { mutableStateOf(members.firstOrNull()) }
    var payerExpanded by remember { mutableStateOf(false) }

    val participantSelected = remember {
        mutableStateMapOf(*members.map { it.id to true }.toTypedArray())
    }

    val customAmounts = remember {
        mutableStateMapOf<Long, String>()
    }

    var splitMode by remember { mutableStateOf(SplitMode.EQUAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add expense") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Total amount") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = payerExpanded,
                    onExpandedChange = { payerExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedPayer?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Paid by") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(payerExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = payerExpanded,
                        onDismissRequest = { payerExpanded = false }
                    ) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.name) },
                                onClick = {
                                    selectedPayer = member
                                    payerExpanded = false
                                }
                            )
                        }
                    }
                }

                Text("Split mode", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = splitMode == SplitMode.EQUAL,
                        onClick = { splitMode = SplitMode.EQUAL },
                        label = { Text("Equal") }
                    )
                    FilterChip(
                        selected = splitMode == SplitMode.CUSTOM,
                        onClick = { splitMode = SplitMode.CUSTOM },
                        label = { Text("Custom") }
                    )
                }

                Text("Participants", style = MaterialTheme.typography.labelLarge)
                members.forEach { member ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = participantSelected[member.id] == true,
                            onCheckedChange = { participantSelected[member.id] = it }
                        )
                        Text(
                            text = member.name,
                            modifier = Modifier.weight(1f)
                        )
                        if (splitMode == SplitMode.CUSTOM && participantSelected[member.id] == true) {
                            OutlinedTextField(
                                value = customAmounts[member.id] ?: "",
                                onValueChange = { customAmounts[member.id] = it },
                                label = { Text("Amount") },
                                singleLine = true,
                                modifier = Modifier.width(100.dp)
                            )
                        } else if (splitMode == SplitMode.EQUAL && participantSelected[member.id] == true) {
                            val selectedCount = participantSelected.values.count { it }
                            val amountDouble = amount.toDoubleOrNull() ?: 0.0
                            val share = if (selectedCount > 0) amountDouble / selectedCount else 0.0
                            Text(
                                text = "%.2f€".format(share),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amountDouble = amount.toDoubleOrNull() ?: return@TextButton
                val payer = selectedPayer ?: return@TextButton
                if (description.isBlank()) return@TextButton

                val selectedMembers = participantSelected.filter { it.value }.keys.toList()
                if (selectedMembers.isEmpty()) return@TextButton

                val participants = when (splitMode) {
                    SplitMode.EQUAL -> {
                        val share = amountDouble / selectedMembers.size
                        selectedMembers.map { it to share }
                    }
                    SplitMode.CUSTOM -> {
                        // Separamos los que tienen cantidad manual de los que no
                        val withCustom = selectedMembers.filter { memberId ->
                            customAmounts[memberId]?.toDoubleOrNull() != null
                        }
                        val withoutCustom = selectedMembers.filter { memberId ->
                            customAmounts[memberId]?.toDoubleOrNull() == null
                        }

                        // Restamos las cantidades manuales del total
                        val assignedAmount = withCustom.sumOf { customAmounts[it]!!.toDouble() }
                        val remaining = amountDouble - assignedAmount

                        // Si las cantidades manuales superan el total, no confirmamos
                        if (remaining < -0.01) return@TextButton

                        // El resto se reparte equitativamente entre los sin cantidad manual
                        val shareForRest = if (withoutCustom.isNotEmpty()) remaining / withoutCustom.size else 0.0

                        withCustom.map { it to customAmounts[it]!!.toDouble() } +
                                withoutCustom.map { it to shareForRest }
                    }
                }

                onConfirm(payer.id, description, amountDouble, participants)
            }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private enum class SplitMode { EQUAL, CUSTOM }