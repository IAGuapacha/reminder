package com.iaguapacha.reminder.ui.birthdayform

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iaguapacha.reminder.R

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayFormScreen(
    navController: NavController,
    birthdayId: Long? = null,
    viewModel: BirthdayFormViewModel = hiltViewModel()
) {
    // Si se proporciona un ID de cumpleaños, cargar los datos para edición
    LaunchedEffect(birthdayId) {
        birthdayId?.let { viewModel.loadBirthday(it) }
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.navigationEvent) {
        when (state.navigationEvent) {
            NavigationEvent.Back -> {
                navController.popBackStack()
                viewModel.handleEvent(BirthdayFormEvent.Navigated)
            }
            null -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            id = if (state.isEditMode) R.string.edit_birthday_title
                            else R.string.add_birthday_title
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.person),
                        modifier = Modifier.size(50.dp)
                    )
                }

                NameField(
                    value = state.name,
                    onValueChange = { viewModel.handleEvent(BirthdayFormEvent.NameChanged(it)) },
                    error = state.nameError,
                    label = stringResource(id = R.string.name)
                )

                DatePicker(
                    day = state.day,
                    month = state.month,
                    year = state.year,
                    onDayChanged = { viewModel.handleEvent(BirthdayFormEvent.DayChanged(it)) },
                    onMonthChanged = { viewModel.handleEvent(BirthdayFormEvent.MonthChanged(it)) },
                    onYearChanged = { viewModel.handleEvent(BirthdayFormEvent.YearChanged(it)) },
                    dayError = state.dayError,
                    monthError = state.monthError,
                    yearError = state.yearError,
                    label = stringResource(id = R.string.birthday_date),
                    note = stringResource(id = R.string.note_year_optional),
                    dayLabel = stringResource(id = R.string.day),
                    monthLabel = stringResource(id = R.string.month),
                    yearLabel = stringResource(id = R.string.year)
                )

                NotificationSelection(
                    selected = state.notifications,
                    onSelectedChange = {
                        viewModel.handleEvent(
                            BirthdayFormEvent.NotificationToggled(
                                it
                            )
                        )
                    }
                )
            }
        },
        bottomBar = {
            SaveButton(
                isLoading = state.isLoading,
                isEditMode = state.isEditMode,
                onSave = { viewModel.handleEvent(BirthdayFormEvent.Save) }
            )
        }
    )
}

@Composable
fun SaveButton(
    isLoading: Boolean,
    isEditMode: Boolean,
    onSave: () -> Unit
) {
    Button(
        onClick = onSave,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(
                stringResource(
                    id = if (isEditMode) R.string.update else R.string.save
                )
            )
        }
    }
}

@Composable
fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text(label) },
        isError = error != null,
        supportingText = { ErrorText(error) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words
        )
    )
}

@Composable
fun NotificationSelection(
    selected: Set<NotificationType>,
    onSelectedChange: (NotificationType) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(id = R.string.notifications), style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        NotificationType.entries.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .clickable { onSelectedChange(type) }
                    .padding(vertical = 2.dp)
            ) {
                Checkbox(
                    checked = selected.contains(type),
                    onCheckedChange = { onSelectedChange(type) }
                )
                Text(stringResource(id = when(type) {
                    NotificationType.ON_DATE -> R.string.on_date
                    NotificationType.TWO_DAYS_BEFORE -> R.string.two_days_before
                    NotificationType.ONE_WEEK_BEFORE -> R.string.one_week_before
                }), modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun DatePicker(
    day: String,
    month: String,
    year: String,
    onDayChanged: (String) -> Unit,
    onMonthChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,
    dayError: String?,
    monthError: String?,
    yearError: String?,
    label: String,
    note: String,
    dayLabel: String,
    monthLabel: String,
    yearLabel: String
) {
    Column() {
        Text(
            label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            note,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier
                .fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)

        ) {
            // Campo Día
            OutlinedTextField(
                value = day,
                onValueChange = onDayChanged,
                label = { Text(dayLabel) },
                modifier = Modifier.weight(1f),
                isError = dayError != null,
                supportingText = { ErrorText(dayError) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            // Campo Mes
            OutlinedTextField(
                value = month,
                onValueChange = onMonthChanged,
                label = { Text(monthLabel) },
                modifier = Modifier.weight(1f),
                isError = monthError != null,
                supportingText = { ErrorText(monthError) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            // Campo Año
            OutlinedTextField(
                value = year,
                onValueChange = onYearChanged,
                label = { Text(yearLabel) },
                modifier = Modifier.weight(1f),
                isError = yearError != null,
                supportingText = { ErrorText(yearError) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}

@Composable
private fun ErrorText(error: String?) {
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
