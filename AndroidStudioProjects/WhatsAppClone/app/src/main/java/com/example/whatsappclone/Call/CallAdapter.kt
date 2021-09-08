package com.example.whatsappclone.Call

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.whatsappclone.R
import com.example.whatsappclone.ui.main.toHumanDate

class CallAdapter : BaseAdapter() {
    val items:MutableList<Call> = mutableListOf()
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
        if(p1 == null){
            val inflater = p2?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.call_layout,p2,false)
            val call = items[p0]
            val callOutIn = view.findViewById<ImageView>(R.id.callOutInView)
            val name = view.findViewById<TextView>(R.id.callContactName)
            val date = view.findViewById<TextView>(R.id.callDate)
            date.text = toHumanDate(call.date)
            name.text = items[p0].contact.name

            if(call.callDirection == CallDirection.INCOMING){
                callOutIn.rotation = 180F
            }
            if(call.callResult == CallResult.MISSED){
                callOutIn.setColorFilter(Color.RED)
            }
            return view
        }
        return p1


    }

    fun addNews(news:Collection<Call>){
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
    fun add(call: Call){
        if(!items.contains(call)){
            items.add(call)
            notifyDataSetChanged()
        }
    }
    fun remove(call: Call):Boolean{
        val res = items.remove(call)
        if(res){
            notifyDataSetChanged()
        }
        return res
    }
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}