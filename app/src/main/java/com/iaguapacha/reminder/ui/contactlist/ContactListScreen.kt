package com.iaguapacha.reminder.ui.contactlist

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.iaguapacha.reminder.data.model.Contact
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val context = LocalContext.current
    val contactPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }


    if (contactPermissionState.status.isGranted) {
        viewModel.getAllContacts()

        val uiState by viewModel.uiState.collectAsState()
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            }

            uiState.errorMessage != null -> {
//                ErrorScreen(
//                    message = uiState.errorMessage,
//                    onRetry = { viewModel.loadContacts() }
//                )
            }

            else -> {
                ContactsList(
                    contacts = uiState.contacts,
                    modifier = modifier
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Permiso no concedido
            Text("Necesitamos acceso a tus contactos para continuar.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { contactPermissionState.launchPermissionRequest() }) {
                Text("Conceder permiso")
            }

            // Si el permiso fue denegado permanentemente
            if (!contactPermissionState.status.shouldShowRationale && !contactPermissionState.status.isGranted) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("El permiso fue denegado permanentemente. Por favor, otórgalo manualmente en la configuración.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { openAppSettings() }) {
                    Text("Abrir configuración")
                }
            }

        }
    }
}

@Composable
fun ContactsList(
    contacts: List<Contact>,
    modifier: Modifier
) {
    LazyColumn (
        modifier = modifier
    ) {
        items(contacts) { contact ->
            ContactRow(
                name = contact.name,
                phoneNumber = contact.phone,
                initials = getInitials(contact.name),
                backgroundColor = getBackgroundColor(contact.name),
                textColor = Color.White
            )
        }
    }
}

fun getInitials(name: String): String {
    val words = name.trim().split("\\s+".toRegex())
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(1).uppercase()
        else -> (words[0].take(1) + words.last().take(1)).uppercase()
    }
}

fun getBackgroundColor(name: String): Color {
        val colors = listOf(
            Color.Red, Color.Blue, Color.Green, Color.Cyan,
            Color.Magenta, Color.Yellow, Color.Magenta, Color.Gray
        )
        val index = (name.hashCode() and 0x7FFFFFFF) % colors.size
        return colors[index]
}

@Composable
fun ContactInitialsCircle(
    initials: String,
    size: Dp = 48.dp, // Tamaño del círculo
    backgroundColor: Color = Color.Gray,
    textColor: Color = Color.White,
    fontSize: TextUnit = 16.sp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials.uppercase(),
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactInitialsCirclePreview() {
    ContactInitialsCircle(
        initials = "JC",
        size = 64.dp,
        backgroundColor = Color.Blue,
        textColor = Color.White,
        fontSize = 20.sp
    )
}

@Composable
fun ContactRow(
    name: String,
    phoneNumber: String,
    initials: String,
    backgroundColor: Color,
    textColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ContactInitialsCircle(
            initials = initials,
            backgroundColor = backgroundColor,
            textColor = textColor
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = phoneNumber,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ContactRowPreview() {

    ContactRow(
        name = "John Doe",
        phoneNumber = "+1 234 567 890",
        initials = "JD",
        backgroundColor = Color.Blue,
        textColor = Color.White
    )
}