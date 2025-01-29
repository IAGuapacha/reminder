package com.iaguapacha.reminder.data.datasource

import android.content.Context
import android.provider.ContactsContract
import com.iaguapacha.reminder.data.model.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceContactsSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getDeviceContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver = context.contentResolver
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val sortOrder = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        val cursor = contentResolver.query(uri, projection, null, null, sortOrder)
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Contact(name = name, phone = number, email = null))
            }
        }
        return contacts
    }
}
