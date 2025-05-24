package com.iaguapacha.reminder.ui.birthdaydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailScreen(navController: NavController, contactId: Long) {
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
            Detail(modifier = Modifier.padding(padding))
        }
    )
}


@Composable
fun Detail(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(top = 60.dp)) {

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
            "Camila",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 12.dp, bottom = 12.dp),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            "Viernes  5 de Agosto, 2000",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            "125 Días",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(50.dp),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 50.dp)
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

