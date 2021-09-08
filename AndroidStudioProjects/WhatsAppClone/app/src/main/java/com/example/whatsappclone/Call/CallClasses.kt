package com.example.whatsappclone.Call

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.whatsappclone.ui.main.Contact
import com.example.whatsappclone.ui.main.Message
import com.example.whatsappclone.ui.main.baseMessage
import java.time.LocalDate




/*
* 
* Dummy Data
* 
* 
* */

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


/*
* 
* 
* Dummy data Ends
* 
* 
* */

enum class CallType {
    VOICE,VIDEO
}
enum class CallDirection {
    INCOMING,OUTGOING
}

enum class CallResult{
    ANSWERED,MISSED
}


data class Call(
    val id:Int,
    val contact: Contact,
    val date:LocalDate,
    val callType: CallType,
    val callDirection: CallDirection,
    val callResult: CallResult,
)
val baseCall = Call(0, contacts[0], contacts[0].messages[0].date,CallType.VIDEO,CallDirection.INCOMING,CallResult.MISSED)
val dummyCalls = listOf<Call>(
    baseCall,
    baseCall.copy(id = 1,callType = CallType.VOICE,callDirection =CallDirection.OUTGOING,callResult = CallResult.ANSWERED),
    baseCall.copy(id = 2,callType = CallType.VOICE,callDirection =CallDirection.OUTGOING,callResult = CallResult.ANSWERED),
    baseCall.copy(id = 3,callResult = CallResult.ANSWERED),
    baseCall.copy(id = 4,callResult = CallResult.ANSWERED),
    baseCall.copy(id = 5),
    baseCall.copy(id = 6,callType = CallType.VOICE,callDirection =CallDirection.OUTGOING,callResult = CallResult.ANSWERED),
    baseCall.copy(id = 7,callType = CallType.VOICE,callDirection =CallDirection.OUTGOING)

)

