package com.iaguapacha.reminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.iaguapacha.reminder.ui.mainscreen.MainScreen
import com.iaguapacha.reminder.ui.addbirthday.AddBirthdayScreen
import com.iaguapacha.reminder.ui.birthdaydetail.BirthdayDetailScreen
import com.iaguapacha.reminder.ui.theme.ReminderTheme
import com.iaguapacha.reminder.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReminderTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.Main.route) {
        composable(NavRoutes.Main.route) { MainScreen(navController) }
        composable(NavRoutes.AddBirthday.route) { AddBirthdayScreen(navController) }
        composable(
            NavRoutes.BirthdayDetail.route,
            arguments = listOf(navArgument("reminderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getLong("reminderId")
            BirthdayDetailScreen(navController, contactId ?: 0)
        }
    }
}
