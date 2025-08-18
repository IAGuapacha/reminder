package com.iaguapacha.reminder.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BirthdayDatePicker(
    modifier: Modifier = Modifier,
    initialDate: LocalDate? = null,
    onDateSelected: (day: Int?, month: Int?, year: Int?) -> Unit
) {
    val today = LocalDate.now()
    val dateToUse = initialDate ?: today

    var selectedTab by remember { mutableStateOf(0) }
    var selectedYear by remember { mutableStateOf<Int?>(dateToUse.year) }
    var selectedMonth by remember { mutableStateOf(dateToUse.monthValue) }
    var selectedDay by remember { mutableStateOf(dateToUse.dayOfMonth) }

    val tabs = listOf("Año", "Mes", "Día")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF111111))
            .padding(16.dp)
    ) {
        Text(
            text = buildString {
                append(selectedDay.toString())
                append(" de ")
                append(
                    selectedMonth.let { m -> monthNames[m - 1] }
                )
                selectedYear?.let { year ->
                    append(" de ")
                    append(year.toString())
                }
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF222222)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, color = Color.White) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenedor con altura fija para el contenido de los tabs
        Box(
            modifier = Modifier
                .height(280.dp) // Altura fija para el contenido
                .fillMaxWidth()
        ) {
            when (selectedTab) {
                0 -> { // Año
                    val years = (today.year downTo today.year - 100).toList() // últimos 100 años
                    val yearsWithNone = listOf<Int?>(null) + years
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(yearsWithNone) { year ->
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        selectedYear = year
                                    }
                                    .background(
                                        if (selectedYear == year) Color(0xFF3F51B5) else Color.Transparent
                                    )
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = year?.toString() ?: "---",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                1 -> { // Mes
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items((1..12).toList()) { month ->
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { selectedMonth = month }
                                    .background(
                                        if (selectedMonth == month) Color(0xFF3F51B5) else Color.Transparent
                                    )
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = monthNames[month - 1],
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                2 -> { // Día
                    val maxDay = YearMonth.of(selectedYear ?: today.year, selectedMonth).lengthOfMonth()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items((1..maxDay).toList()) { day ->
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clickable { selectedDay = day }
                                    .background(
                                        if (selectedDay == day) Color(0xFF3F51B5) else Color.Transparent
                                    )
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Botón siempre visible en la parte inferior
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onDateSelected(selectedDay, selectedMonth, selectedYear) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
        ) {
            Text(
                text = "Guardar Fecha",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

private val monthNames = listOf(
    "Ene", "Feb", "Mar", "Abr", "May", "Jun",
    "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
)