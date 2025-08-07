package com.iaguapacha.reminder.utils

import java.util.Calendar

object DateUtils {

    fun daysUntilBirthday(day: Int, month: Int): Int {
        val today = Calendar.getInstance()
        // Establecer today a las 00:00:00.000 del día actual
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

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
}