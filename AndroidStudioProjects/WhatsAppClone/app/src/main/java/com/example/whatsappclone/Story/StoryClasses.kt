package com.example.whatsappclone.Story

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.whatsappclone.ui.main.Contact
import com.example.whatsappclone.ui.main.Message
import java.time.LocalDate
@RequiresApi(Build.VERSION_CODES.O)
data class StoryData(
    val id:Int,
    val count:Int,
    val contact:Contact,
    var seenCount:Int = 0,
    val date:LocalDate = LocalDate.now()
)

@RequiresApi(Build.VERSION_CODES.O)
val emptyStoryData = StoryData(-1,0,Contact(
    id = -1,
    name = "",
    null,
    messages = mutableListOf<Message>()
))
