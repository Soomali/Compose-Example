package com.example.whatsappclone.ui.main

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.whatsappclone.R
import com.mikhaellopez.circularimageview.CircularImageView
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun toHumanDate(date: LocalDate):String{
    return "${date.dayOfMonth}.${date.month}.${date.year}"
}
class ContactAdapter(    private val contacts:MutableList<Contact> = mutableListOf()) : BaseAdapter(){
    override fun getCount(): Int {
       return contacts.size
    }

    override fun getItem(p0: Int): Any {
        return contacts[p0]
    }

    override fun getItemId(p0: Int): Long {
        return contacts[p0].id.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        lateinit var view:View
        Log.d("[Invoke Function]","getView($p0,$p1,$p2)")
        val inflater = p2?.context?.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if(p1 == null){
            view = inflater.inflate(R.layout.contact_layout, p2,false)
            (view.findViewById(R.id.nameView) as TextView).text = contacts[p0].name
            (view.findViewById(R.id.MessageView) as TextView).text = contacts[p0].messages[0].text
            (view.findViewById(R.id.dateView) as TextView).text = toHumanDate(contacts[p0].messages[0].date)
            if(contacts[p0].photo != null){
            //(view.findViewById(R.id.imageView) as CircularImageView).setImageBitmap(contacts[p0].photo)
            }
            return view
        }
        return p1
    }
    fun addNews(news:Collection<Contact>){
        Log.d("[Stuck]","TRUE")
        var hasChanged = false
        for (i in news){
            if(!contacts.contains(i)){
                contacts.add(i)
                hasChanged = true
            }
        }
        if(hasChanged){
            notifyDataSetChanged()
        }
    }
    fun add(contact:Contact){
        if(!contacts.contains(contact)){
            contacts.add(contact)
            notifyDataSetChanged()
        }
    }
    fun remove(contact:Contact):Boolean{
        val res = contacts.remove(contact)
        if(res){
            notifyDataSetChanged()
        }
        return res
    }

}