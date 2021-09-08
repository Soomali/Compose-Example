package com.example.whatsappclone.Story

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.devlomi.circularstatusview.CircularStatusView
import com.example.whatsappclone.R
import com.example.whatsappclone.UserData
import com.example.whatsappclone.ui.main.Contact
import com.example.whatsappclone.ui.main.Message
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
private fun toHumanDate(date: LocalDate):String{
    return "${date.dayOfMonth}.${date.month}.${date.year}"
}
class StoryAdapter: BaseAdapter() {
    private val items = mutableListOf<StoryData>(
    )
    init {
        
        val data = if(UserData.storyData != null) UserData.storyData else emptyStoryData
        items.add(emptyStoryData)
    }
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return items[p0].id.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        lateinit var view:View
        Log.d("[Invoke Function]","getView($p0,$p1,$p2)")
        val inflater = p2?.context?.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if(p0 == 0) {
            view = inflater.inflate(R.layout.self_story_layout,p2,false)
            view.findViewById<CircularStatusView>(R.id.storyStatusView).visibility = View.VISIBLE
            if(UserData.storyData == null){
                view.findViewById<ImageView>(R.id.storyAddImageView).visibility = View.VISIBLE
                view.findViewById<CircularStatusView>(R.id.storyStatusView).visibility = View.INVISIBLE
            }
            view.findViewById<CircularStatusView>(R.id.storyStatusView).setPortionsCount(
                if(UserData.storyData == null) 1 else UserData.storyData!!.count
            )
            view.findViewById<FrameLayout>(R.id.selfStory).setOnClickListener {
                Log.d("[Story Data]","Clicked itself, ${UserData.storyData}")
            }
            return view
        }
        if(p1 == null){
            view = inflater.inflate(R.layout.story_layout, p2,false)
            view.findViewById<TextView>(R.id.StoryDateView).text = toHumanDate(items[p0].date)
            view.findViewById<TextView>(R.id.storyNameView).text = items[p0].contact.name
            val count = if(items[p0].count > 8) 8 else items[p0].count
            val circularStatusView = view.findViewById<CircularStatusView>(R.id.storyStatusView)
            circularStatusView.setPortionsCount(count)
            for(i in 0..items[p0].seenCount){
                circularStatusView.setPortionColorForIndex(i,Color.GRAY)
            }
            if(items[p0].contact.photo != null){
                //(view.findViewById(R.id.storyImageView) as CircularImageView).setImageBitmap(contacts[p0].photo)
            }
            view.findViewById<FrameLayout>(R.id.storyLayout).setOnClickListener {
                if(items[p0].count > items[p0].seenCount){
                    circularStatusView.setPortionColorForIndex(items[p0].seenCount, Color.GRAY)
                    items[p0].seenCount++
                }
            }
            return view
        }
        return p1

    }
    fun addNews(news:Collection<StoryData>){
        Log.d("[Stuck]","TRUE")
        var hasChanged = false
        for (i in news){
            if(!items.contains(i)){
                items.add(i)
                hasChanged = true
            }
        }
        if(hasChanged){
            notifyDataSetChanged()
        }
    }
    fun add(story: StoryData){
        if(!items.contains(story)){
            items.add(story)
            notifyDataSetChanged()
        }
    }
    fun remove(story: StoryData):Boolean{
        val res = items.remove(story)
        if(res){
            notifyDataSetChanged()
        }
        return res
    }
}