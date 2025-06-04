package com.iaguapacha.reminder.repository

import com.iaguapacha.reminder.data.dao.ContactDao
import com.iaguapacha.reminder.data.model.ContactEntity
import com.iaguapacha.reminder.data.model.ContactWithNotifications
import com.iaguapacha.reminder.data.model.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) {

    suspend fun insertContact(contact: ContactEntity): Long {
        return contactDao.insertContact(contact)
    }

    suspend fun insertNotification(notification: NotificationEntity) {
        contactDao.insertNotification(notification)
    }

    fun getContacts(): Flow<List<ContactEntity>> {
        return contactDao.getContacts()
    }

    suspend fun getDetailContact(contactId: Long): ContactWithNotifications {
        return contactDao.getContactWithNotifications(contactId)
    }
}