package com.iaguapacha.reminder.repository

import com.iaguapacha.reminder.data.dao.ContactDao
import com.iaguapacha.reminder.data.datasource.DeviceContactsSource
import com.iaguapacha.reminder.data.model.Contact
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val deviceContactsSource: DeviceContactsSource
) {

    suspend fun getAllContacts(): List<Contact> {
        return deviceContactsSource.getDeviceContacts()
    }

//    suspend fun syncContacts(deviceContacts: List<Contact>) {
//        val dbContacts = contactDao.getAllContacts()
//
//        // Lógica de sincronización (similar a lo explicado antes)
//        val newContacts = deviceContacts.filter { it.phone !in dbContacts.map { db -> db.phone } }
//        val deletedContacts = dbContacts.filter { it.phone !in deviceContacts.map { dev -> dev.phone } }
//        val updatedContacts = deviceContacts.filter {
//            val dbContact = dbContacts.find { db -> db.phone == it.phone }
//            dbContact != null && dbContact.name != it.name
//        }
//
//        // Aplicar cambios a la base de datos
////        contactDao.insertAll(newContacts)
////        contactDao.deleteAll(deletedContacts)
////        contactDao.insertAll(updatedContacts)
//
//    }
}