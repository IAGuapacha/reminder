package com.iaguapacha.reminder.ui.addbirthday

import android.app.DatePickerDialog
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBirthdayScreen(
    navController: NavController,
    viewModel: AddBirthdayViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.navigationEvent) {
        when (state.navigationEvent) {
            NavigationEvent.Back -> {
                navController.popBackStack()
                viewModel.handleEvent(AddBirthdayEvent.Navigated)
            }

            null -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar cumpleaños") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
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
                        contentDescription = "Persona",
                        modifier = Modifier.size(50.dp)
                    )
                }

                NameField(
                    value = state.name,
                    onValueChange = { viewModel.handleEvent(AddBirthdayEvent.NameChanged(it)) },
                    error = state.nameError
                )

                DatePickerV3(
                    day = state.day,
                    month = state.month,
                    year = state.year,
                    onDayChanged = { viewModel.handleEvent(AddBirthdayEvent.DayChanged(it)) },
                    onMonthChanged = { viewModel.handleEvent(AddBirthdayEvent.MonthChanged(it)) },
                    onYearChanged = { viewModel.handleEvent(AddBirthdayEvent.YearChanged(it)) },
                    dayError = state.dayError,
                    monthError = state.monthError,
                    yearError = state.yearError
                )

                NotificationSelection(
                    selected = state.notifications,
                    onSelectedChange = {
                        viewModel.handleEvent(
                            AddBirthdayEvent.NotificationToggled(
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
                onSave = { viewModel.handleEvent(AddBirthdayEvent.Save) }
            )

        }
    )
}


@Composable
fun SaveButton(
    isLoading: Boolean,
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
            Text("Guardar")
        }
    }
}

@Composable
fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text("Nombre") },
        isError = error != null,
        supportingText = { ErrorText(error) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words
        )
    )
}


enum class NotificationType(val label: String) {
    ON_DATE("En la fecha"),
    TWO_DAYS_BEFORE("2 días antes"),
    ONE_WEEK_BEFORE("1 semana antes")
}

@Composable
fun NotificationSelection(
    selected: Set<NotificationType>,
    onSelectedChange: (NotificationType) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Notificaciones", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        NotificationType.entries.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectedChange(type) }
            ) {
                Checkbox(
                    checked = selected.contains(type),
                    onCheckedChange = { onSelectedChange(type) }
                )
                Text(type.label, modifier = Modifier.padding(start = 8.dp))
            }

        }
    }
}

@Composable
fun DatePickerV3(
    day: String,
    month: String,
    year: String,
    onDayChanged: (String) -> Unit,
    onMonthChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,
    dayError: String?,
    monthError: String?,
    yearError: String?
) {

    Column() {
        Text(
            "Fecha de cumpleaños",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            "Nota: El año es opcional",
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
                label = { Text("Día") },
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
                label = { Text("Mes") },
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
                label = { Text("Año") },
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
