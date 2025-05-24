package com.iaguapacha.reminder.ui.birthday

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iaguapacha.reminder.data.model.ContactEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun BirthdayScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: BirthdayViewModel = hiltViewModel()
) {

    val contacts by viewModel.contacts.collectAsState()

    LazyColumn(modifier = modifier.fillMaxWidth())
    {
        items(contacts) { contact ->
            CardContacts(contact){
                navController.navigate("birthdayDetail/${contact.id}")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getContacts()
    }
}


@Composable
fun CardContacts(contact: ContactEntity, function: () -> Unit) {
    ElevatedCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp).clickable(
        onClick = function
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
                    contentDescription = "Persona",
                    modifier = Modifier.size(37.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = contact.name)
                Text(text = convertDate(contact.day, contact.month, contact.year ?: 0))
                Text(text = "Dias para el cumpleaños")
            }
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
