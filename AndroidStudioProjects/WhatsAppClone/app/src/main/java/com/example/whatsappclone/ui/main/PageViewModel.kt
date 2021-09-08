package com.example.whatsappclone.ui.main

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.util.*


data class Contact(
    val id:Int,
    val name:String,
    val photo: Bitmap?,
    val messages:MutableList<Message>
)

data class Message(
    val text:String,
    val date: LocalDate,
    val fromUser:Boolean
)
@RequiresApi(Build.VERSION_CODES.O)
val baseMessage = Message(text = "naber",date = LocalDate.now(),fromUser = false)
@RequiresApi(Build.VERSION_CODES.O)
val baseContact = Contact(id = 0,name = "ben",messages = mutableListOf<Message>(
    baseMessage,
    baseMessage.copy(date = LocalDate.now().minusDays(1)) ,
    baseMessage.copy(date = LocalDate.now().minusDays(2),fromUser = false),
    baseMessage.copy(text = "iyi sen",date =LocalDate.now().minusDays(3),fromUser = false)
),photo = null)

private val contacts:List<Contact> = listOf(
    baseContact,
    baseContact.copy(id = 1,name="Buygey coc"),
    baseContact.copy(id = 2,name = "HeHe"),
    baseContact.copy(id = 3,name = "HOHO"),
    baseContact.copy(id = 4,name="Buygey coc"),
    baseContact.copy(id = 5,name = "HeHe"),
    baseContact.copy(id = 6,name = "HOHO"),
    baseContact.copy(id = 7,name="Buygey coc"),
    baseContact.copy(id = 8,name = "HeHe"),
    baseContact.copy(id = 9,name = "HOHO"),
)

class PageViewModel : ViewModel() {

    val users: MutableLiveData<List<Contact>> by lazy {
        MutableLiveData<List<Contact>>().also {
            loadUsers()
        }
    }
    init {
        users.value = contacts
    }
    fun getUsers(): LiveData<List<Contact>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.


    }
}
