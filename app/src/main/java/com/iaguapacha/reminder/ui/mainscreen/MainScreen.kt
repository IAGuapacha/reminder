package com.iaguapacha.reminder.ui.mainscreen


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iaguapacha.reminder.ui.birthday.BirthdayScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController) {

    val navControllerLocal = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(context = LocalContext.current)
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("addBirthday")
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar cumpleaños"
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                TopBarWithHamburgerMenu(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNewChatClick = {

                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navControllerLocal,
                startDestination = "birthdays"
            ) {
                composable("birthdays") { backStackEntry ->
                    BirthdayScreen(modifier = Modifier.padding(innerPadding), navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithHamburgerMenu(
    onMenuClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Cumpleaños", style = MaterialTheme.typography.headlineMedium) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menú")
            }
        },
        actions = {
            IconButton(onClick = onNewChatClick) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo cumpleaños")
            }
        }
    )
}

@Composable
fun DrawerContent(context: Context) {
    ModalDrawerSheet(
        modifier = Modifier.widthIn(max = 320.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Cabecera del menú
                Text(
                    text = "Cumpleaños",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Elementos de menú personalizados con espaciado reducido
                CustomDrawerItem(
                    icon = Icons.Default.AccountBox,
                    text = "Importar desde contactos",
                    onClick = { /* Acción para importar contactos */ }
                )

                CustomDrawerItem(
                    icon = Icons.Default.DateRange,
                    text = "Importar desde Calendario",
                    onClick = { /* Acción para importar calendario */ }
                )

                CustomDrawerItem(
                    icon = Icons.Default.Settings,
                    text = "Configuración",
                    onClick = { /* Acción para ir a configuracion */ }
                )

                CustomDrawerItem(
                    icon = Icons.Default.Share,
                    text = "Compartir aplicación",
                    onClick = { shareApp(context) }
                )

                CustomDrawerItem(
                    icon = Icons.Default.Star,
                    text = "Calificar aplicación",
                    onClick = { /* Acción para calificar */ }
                )
            }

            // Links de pie de página
            Column {
                HorizontalDivider()

                TextButton(
                    onClick = { /* Acción para términos y condiciones */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Términos y condiciones",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                TextButton(
                    onClick = { /* Acción para políticas de privacidad */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "Políticas de privacidad",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDrawerItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

fun shareApp(context: Context) {
    val appPackageName = context.packageName
    val shareText = "¡Hola! Te recomiendo esta aplicación para recordar cumpleaños. " +
            "¡No te olvidarás de ningún cumpleaños importante! " +
            "Descárgala en: https://play.google.com/store/apps/details?id=$appPackageName"

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "App de Recordatorio de Cumpleaños")
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
}

