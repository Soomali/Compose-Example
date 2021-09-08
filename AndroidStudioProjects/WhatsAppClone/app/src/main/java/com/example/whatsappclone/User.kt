package com.example.whatsappclone

import android.graphics.Bitmap
import com.example.whatsappclone.Story.StoryData


object UserData{
    var id:Int= -1
    var name:String = ""
    var number:String = ""
    var privacySettings: List<PrivacySetting>? = null
    var photo: Bitmap? = null
    var storyData:StoryData? = null
}
enum class PrivacyChoice{
    PRIVATE,FRIENDS_ONLY,PUBLIC
}
data class PrivacySetting(
    val name:String,
    var choice:PrivacyChoice
)





