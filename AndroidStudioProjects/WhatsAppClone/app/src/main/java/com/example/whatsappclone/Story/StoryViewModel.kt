package com.example.whatsappclone.Story

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsappclone.ui.main.Contact
import com.example.whatsappclone.ui.main.Message
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
val base = StoryData(
id = 0,count = 1,seenCount = 0,
    contact = Contact(
        id = 0,
        name = "BuyGey",
        photo = null,
        messages = mutableListOf<Message>(
            Message(text = "naber",date = LocalDate.now(),fromUser = true)
        )
    )
)
val testStories = listOf<StoryData>(
    base,
    base.copy(
        id = 1,
        count = 5
    ),
    base.copy(
        id = 2,
        count = 3,
        seenCount = 2
    )
)


class StoryViewModel : ViewModel() {
    val stories: MutableLiveData<List<StoryData>> by lazy {
        MutableLiveData<List<StoryData>>().also {
            loadStories()
        }
    }
    init {
        stories.value = testStories
    }
    fun getStories(): LiveData<List<StoryData>> {
        return stories
    }

    private fun loadStories() {
        // Do an asynchronous operation to fetch stories.


    }
}