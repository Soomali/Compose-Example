package com.mehmetali.dialogapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.mehmetali.dialogapp.databinding.CommentLayoutBinding

class ImageAdapter(private val applicationContext: Context) :BaseAdapter() {
    private val items:MutableList<Int> = mutableListOf(1,23,4,5,6,4,6,5,6,1,65,6,6)
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return items[0].hashCode().toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        if(p1 == null) {
            val inflater = p2?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val binding = CommentLayoutBinding.inflate(inflater)
            for (i in 0 until (p0 + 4).rem(4) + 1){
                val view = View(applicationContext)
                val params = LinearLayout.LayoutParams(5,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                params.setMargins(10,0,10,0)
                view.layoutParams = params
                view.setBackgroundColor(Color.MAGENTA)
                binding.commentLayout.addView(view,i)
            }
            return binding.root
        }
        return p1
    }
}
