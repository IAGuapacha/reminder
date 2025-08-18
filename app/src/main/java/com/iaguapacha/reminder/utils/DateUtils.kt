package com.iaguapacha.reminder.utils

import java.util.Calendar
import java.util.concurrent.TimeUnit

object DateUtils {

    private val WEEKDAYS = mapOf(
        Calendar.SUNDAY to "Domingo",
        Calendar.MONDAY to "Lunes",
        Calendar.TUESDAY to "Martes",
        Calendar.WEDNESDAY to "Miércoles",
        Calendar.THURSDAY to "Jueves",
        Calendar.FRIDAY to "Viernes",
        Calendar.SATURDAY to "Sábado"
    )

    private val MONTHS = mapOf(
        1 to "Enero", 2 to "Febrero", 3 to "Marzo",
        4 to "Abril", 5 to "Mayo", 6 to "Junio",
        7 to "Julio", 8 to "Agosto", 9 to "Septiembre",
        10 to "Octubre", 11 to "Noviembre", 12 to "Diciembre"
    )

    fun daysUntilBirthday(day: Int, month: Int): Int {
        val today = createCalendarAtMidnight()
        val birthday = createCalendarAtMidnight().apply {
            set(Calendar.MONTH, month - 1) // Calendar.MONTH is 0-based
            set(Calendar.DAY_OF_MONTH, day)
        }

        if (birthday.before(today)) {
            birthday.add(Calendar.YEAR, 1)
        }

        val diffMillis = birthday.timeInMillis - today.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
    }

    fun formatDate(day: Int, month: Int, year: Int? = null): String {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val calendar = createCalendarAtMidnight().apply {
            set(Calendar.YEAR, currentYear)
            set(Calendar.MONTH, month - 1) // Calendar.MONTH is 0-based
            set(Calendar.DAY_OF_MONTH, day)
        }

        val weekdayName = WEEKDAYS[calendar.get(Calendar.DAY_OF_WEEK)] ?: "?"
        val monthName = MONTHS[month] ?: "?"

        return if (year != null) {
            "$weekdayName $day de $monthName, $year"
        } else {
            "$weekdayName $day de $monthName"
        }
    }

    fun formatDateWithoutWeekday(day: Int, month: Int, year: Int? = null): String {
        val monthName = MONTHS[month] ?: "?"

        return if (year != null) {
            "$day de $monthName de $year"
        } else {
            "$day de $monthName"
        }
    }

    private fun createCalendarAtMidnight(): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
}