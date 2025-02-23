package com.iaguapacha.reminder.ui.addbirthday

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
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
                    isError = state.error?.let {
                        it == "El nombre no puede estar vacío"
                    } ?: false
                )

                DatePickerV2(
                    selectedDate = state.date,
                    onDateSelected = { viewModel.handleEvent(AddBirthdayEvent.DateChanged(it)) }
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
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        label = {
            Text(
                text = "Nombre",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = "El nombre no puede estar vacío",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = false
        ),
        placeholder = {
            Text(
                text = "Ej: Camila",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
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
                    .padding(8.dp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerV2(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        selectedDate.year,
        selectedDate.monthValue - 1,
        selectedDate.dayOfMonth
    )

    OutlinedButton(
        onClick = { datePickerDialog.show() },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Seleccionar fecha: ${dateFormatter.format(selectedDate)}")
    }

}

