package com.iaguapacha.reminder.ui.birthday

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iaguapacha.reminder.R
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.navigation.NavRoutes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun BirthdayScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: BirthdayViewModel = hiltViewModel()
) {

    val reminders by viewModel.reminders.collectAsState()

    if (reminders.isEmpty()) {
        EmptyBirthdayState(
            modifier = modifier.fillMaxSize(),
            onAddBirthday = {
                navController.navigate(NavRoutes.BirthdayForm.createRoute())
            }
        )
    } else {
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(reminders) { reminder ->
                CardReminder(reminder) {
                    navController.navigate(NavRoutes.BirthdayDetail.createRoute(reminder.id))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getReminders()
    }
}


@Composable
fun CardReminder(reminder: ReminderEntity, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp).clickable(
        onClick = onClick
    )) {
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.person),
                    modifier = Modifier.size(37.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = reminder.name)
                Text(text = convertDate(reminder.day, reminder.month, reminder.year ?: 0))
                Text(text = stringResource(id = R.string.days_until_birthday))
            }
        }
    }
}

@Composable
fun EmptyBirthdayState(
    modifier: Modifier = Modifier,
    onAddBirthday: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = stringResource(id = R.string.person),
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.empty_birthday_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.empty_birthday_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddBirthday) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_birthday),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.add_birthday))
        }
    }
}


fun convertDate(day: Int, month: Int, year: Int): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month - 1)

    val sdf = SimpleDateFormat("d 'de' MMMM", Locale("es", "ES"))
    return sdf.format(calendar.time)
}
