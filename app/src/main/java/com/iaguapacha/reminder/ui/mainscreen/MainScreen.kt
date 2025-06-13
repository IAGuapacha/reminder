package com.iaguapacha.reminder.ui.mainscreen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.iaguapacha.reminder.R
import com.iaguapacha.reminder.navigation.NavRoutes
import com.iaguapacha.reminder.ui.birthday.BirthdayScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavController) {

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
                        navController.navigate(NavRoutes.BirthdayForm.route)
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_birthday)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            topBar = {
                TopBarWithHamburgerMenu(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNewChatClick = {
                        navController.navigate(NavRoutes.BirthdayForm.route)
                    }
                )
            }
        ) { innerPadding ->
            BirthdayScreen(modifier = Modifier.padding(innerPadding), navController)
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
        title = { Text(stringResource(id = R.string.birthday), style = MaterialTheme.typography.headlineMedium) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(id = R.string.menu))
            }
        },
        actions = {
            IconButton(onClick = onNewChatClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.new_birthday))
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
                    text = stringResource(id = R.string.birthday),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Elementos de menú personalizados con espaciado reducido
                CustomDrawerItem(
                    icon = Icons.Default.AccountBox,
                    text = stringResource(id = R.string.import_from_contacts),
                    onClick = { /* Acción para importar contactos */ }
                )

                CustomDrawerItem(
                    icon = Icons.Default.DateRange,
                    text = stringResource(id = R.string.import_from_calendar),
                    onClick = { /* Acción para importar calendario */ }
                )

                CustomDrawerItem(
                    icon = Icons.Default.Settings,
                    text = stringResource(id = R.string.settings),
                    onClick = { /* Acción para ir a configuracion */ }
                )

                CustomDrawerItem(
                    icon = Icons.Default.Share,
                    text = stringResource(id = R.string.share_app),
                    onClick = { shareApp(context) }
                )

                CustomDrawerItem(
                    icon = Icons.Default.Star,
                    text = stringResource(id = R.string.rate_app),
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
                        text = stringResource(id = R.string.terms_and_conditions),
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
                        text = stringResource(id = R.string.privacy_policy),
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
    val shareText = context.getString(R.string.share_app_text, appPackageName)

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_app_subject))
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_via)))
}
