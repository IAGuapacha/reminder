package com.iaguapacha.reminder.ui.birthdaydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iaguapacha.reminder.data.model.ContactWithNotifications
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailScreen(
    navController: NavController,
    contactId: Long,
    viewModel: BirthdayDetailViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData(contactId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
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

            when (state) {
                BirthdayDetailState.Loading -> {

                }

                is BirthdayDetailState.Success -> {

                    Detail(
                        modifier = Modifier.padding(padding),
                        (state as BirthdayDetailState.Success).contactDetail
                    )
                }

                is BirthdayDetailState.Error -> {

                }
            }


        }
    )
}


@Composable
fun Detail(modifier: Modifier, contactDetail: ContactWithNotifications) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 60.dp)
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

        Text(
            contactDetail.contact.name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp, bottom = 12.dp),
            style = MaterialTheme.typography.headlineLarge
        )

        val dateFormat = formatFecha(
            contactDetail.contact.day,
            contactDetail.contact.month,
            contactDetail.contact.year
        )
        Text(
            dateFormat,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge
        )
        val days = daysUntilBirthday(contactDetail.contact.day, contactDetail.contact.month)

        Text(
            "$days Días",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(50.dp),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 50.dp)
        ) {
            RoundedIconButton(
                onClick = { /* Tu acción aquí */ },
                icon = Icons.Default.Delete,
                contentDescription = "Eliminar",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                iconTint = MaterialTheme.colorScheme.onSecondary
            )

            RoundedIconButton(
                onClick = { /* Tu acción aquí */ },
                icon = Icons.Default.Edit,
                contentDescription = "Editar",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                iconTint = MaterialTheme.colorScheme.onSecondary
            )

            RoundedIconButton(
                onClick = { /* Tu acción aquí */ },
                icon = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                iconTint = MaterialTheme.colorScheme.onSecondary
            )

            RoundedIconButton(
                onClick = { /* Tu acción aquí */ },
                icon = Icons.Default.Send,
                contentDescription = "Enviar",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                iconTint = MaterialTheme.colorScheme.onSecondary
            )

        }


    }
}

fun formatFecha(day: Int, month: Int, year: Int? = null): String {
    // Mapas para nombres en español
    val diasSemana = mapOf(
        Calendar.SUNDAY to "Domingo",
        Calendar.MONDAY to "Lunes",
        Calendar.TUESDAY to "Martes",
        Calendar.WEDNESDAY to "Miércoles",
        Calendar.THURSDAY to "Jueves",
        Calendar.FRIDAY to "Viernes",
        Calendar.SATURDAY to "Sábado"
    )
    val meses = mapOf(
        1 to "Enero", 2 to "Febrero", 3 to "Marzo",
        4 to "Abril", 5 to "Mayo", 6 to "Junio",
        7 to "Julio", 8 to "Agosto", 9 to "Septiembre",
        10 to "Octubre", 11 to "Noviembre", 12 to "Diciembre"
    )

    // Año para el cálculo (el actual si no nos dan uno)
    val yearForCalc = year ?: Calendar.getInstance().get(Calendar.YEAR)

    // Creamos un Calendar con la fecha indicada
    val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, yearForCalc)
        set(Calendar.MONTH, month - 1)       // Calendar.MONTH es 0-based
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // Obtenemos los nombres
    val nombreDia = diasSemana[cal.get(Calendar.DAY_OF_WEEK)] ?: "?"
    val nombreMes = meses[month] ?: "?"

    // Construimos el string, incluyendo año solo si se pasó
    return if (year != null) {
        "$nombreDia $day de $nombreMes, $year"
    } else {
        "$nombreDia $day de $nombreMes"
    }
}


fun daysUntilBirthday(day: Int, month: Int): Int {
    val today = Calendar.getInstance()
    val birthday = Calendar.getInstance()

    birthday.set(Calendar.MONTH, month - 1)
    birthday.set(Calendar.DAY_OF_MONTH, day)
    birthday.set(Calendar.HOUR_OF_DAY, 0)
    birthday.set(Calendar.MINUTE, 0)
    birthday.set(Calendar.SECOND, 0)
    birthday.set(Calendar.MILLISECOND, 0)


    if (birthday.before(today)) {
        birthday.add(Calendar.YEAR, 1)
    }

    val diffMillis = birthday.timeInMillis - today.timeInMillis
    return (diffMillis / (1000 * 60 * 60 * 24)).toInt()
}


@Composable
fun RoundedIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    iconTint: Color = MaterialTheme.colorScheme.onPrimary,
    size: Dp = 54.dp,
    iconSize: Dp = 27.dp,
    cornerRadius: Dp = 8.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}

