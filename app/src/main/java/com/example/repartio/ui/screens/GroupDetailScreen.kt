package com.example.repartio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.repartio.R
import com.example.repartio.domain.model.Member
import com.example.repartio.ui.FormatUtils.formatCurrency
import com.example.repartio.ui.theme.CurrencyPreference
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
    val group by viewModel.group.collectAsState()
    val currencyPreference by viewModel.currencyPreference.collectAsState()

    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group?.name ?: "", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
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
                    text = { Text(stringResource(R.string.add_member)) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
                ExtendedFloatingActionButton(
                    onClick = { showAddExpenseDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text(stringResource(R.string.add_expense)) }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                SectionCard(title = stringResource(R.string.members)) {
                    if (members.isEmpty()) {
                        Text(
                            stringResource(R.string.no_members_yet),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            members.forEachIndexed { index, member ->
                                val chipColors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                                val color = chipColors[index % chipColors.size]
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text(member.name) },
                                    icon = {
                                        Surface(
                                            shape = CircleShape,
                                            color = color,
                                            modifier = Modifier.size(8.dp)
                                        ) {}
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                SectionCard(title = stringResource(R.string.expenses)) {
                    if (expenses.isEmpty()) {
                        Text(
                            stringResource(R.string.no_expenses_yet),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            expenses.forEach { expense ->
                                val payerName = members.find { it.id == expense.payerId }?.name ?: ""
                                ExpenseItem(
                                    description = expense.description,
                                    amount = formatCurrency(expense.amount, currencyPreference),
                                    payerName = payerName,
                                    participants = expense.participants.map { participant ->
                                        val name = members.find { it.id == participant.memberId }?.name ?: ""
                                        name to formatCurrency(participant.amountOwed, currencyPreference)
                                    },
                                    onDelete = { viewModel.deleteExpense(expense) }
                                )
                            }
                        }
                    }
                }
            }

            item {
                SectionCard(title = stringResource(R.string.settlements)) {
                    if (settlements.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("✓", style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary)
                            Text(
                                stringResource(R.string.everyone_is_even),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            settlements.forEach { settlement ->
                                SettlementItem(settlement, currencyPreference)
                            }
                        }
                    }
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
            currencyPreference = currencyPreference,
            onConfirm = { payerId, description, amount, participants ->
                viewModel.addExpense(payerId, description, amount, participants)
                showAddExpenseDialog = false
            },
            onDismiss = { showAddExpenseDialog = false }
        )
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    description: String,
    amount: String,
    payerName: String,
    participants: List<Pair<String, String>>,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(
                    stringResource(R.string.paid_by, payerName),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_expense),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (participants.isNotEmpty()) {
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    if (expanded) stringResource(R.string.hide_details)
                    else stringResource(R.string.show_details),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (expanded) {
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    participants.forEach { (name, amount) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(amount, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun SettlementItem(
    settlement: com.example.repartio.domain.usecase.Settlement,
    currencyPreference: CurrencyPreference
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${settlement.fromMemberName} → ${settlement.toMemberName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                formatCurrency(settlement.amount, currencyPreference),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
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
        title = { Text(stringResource(R.string.add_member)) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onConfirm(name) }) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseDialog(
    members: List<Member>,
    currencyPreference: CurrencyPreference,
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
        title = { Text(stringResource(R.string.add_expense)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(stringResource(R.string.total_amount)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                        label = { Text(stringResource(R.string.paid_by_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(payerExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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

                Text(stringResource(R.string.split_mode), style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = splitMode == SplitMode.EQUAL,
                        onClick = { splitMode = SplitMode.EQUAL },
                        label = { Text(stringResource(R.string.split_equally)) }
                    )
                    FilterChip(
                        selected = splitMode == SplitMode.CUSTOM,
                        onClick = { splitMode = SplitMode.CUSTOM },
                        label = { Text(stringResource(R.string.custom_amounts)) }
                    )
                }
                Text(
                    text = if (splitMode == SplitMode.EQUAL)
                        stringResource(R.string.split_equally_hint)
                    else
                        stringResource(R.string.custom_amounts_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(stringResource(R.string.participants), style = MaterialTheme.typography.labelLarge)
                members.forEach { member ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = participantSelected[member.id] == true,
                            onCheckedChange = { participantSelected[member.id] = it }
                        )
                        Text(text = member.name, modifier = Modifier.weight(1f))
                        if (splitMode == SplitMode.CUSTOM && participantSelected[member.id] == true) {
                            OutlinedTextField(
                                value = customAmounts[member.id] ?: "",
                                onValueChange = { customAmounts[member.id] = it },
                                label = { Text(stringResource(R.string.amount)) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.width(100.dp)
                            )
                        } else if (splitMode == SplitMode.EQUAL && participantSelected[member.id] == true) {
                            val selectedCount = participantSelected.values.count { it }
                            val amountDouble = amount.toDoubleOrNull() ?: 0.0
                            val share = if (selectedCount > 0) amountDouble / selectedCount else 0.0
                            Text(
                                text = formatCurrency(share, currencyPreference),
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
                        val withCustom = selectedMembers.filter { memberId ->
                            customAmounts[memberId]?.toDoubleOrNull() != null
                        }
                        val withoutCustom = selectedMembers.filter { memberId ->
                            customAmounts[memberId]?.toDoubleOrNull() == null
                        }
                        val assignedAmount = withCustom.sumOf { customAmounts[it]!!.toDouble() }
                        val remaining = amountDouble - assignedAmount
                        if (remaining < -0.01) return@TextButton
                        if (withoutCustom.isEmpty() && Math.abs(remaining) > 0.01) return@TextButton
                        val shareForRest = if (withoutCustom.isNotEmpty()) remaining / withoutCustom.size else 0.0
                        withCustom.map { it to customAmounts[it]!!.toDouble() } +
                                withoutCustom.map { it to shareForRest }
                    }
                }
                onConfirm(payer.id, description, amountDouble, participants)
            }) { Text(stringResource(R.string.add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

private enum class SplitMode { EQUAL, CUSTOM }