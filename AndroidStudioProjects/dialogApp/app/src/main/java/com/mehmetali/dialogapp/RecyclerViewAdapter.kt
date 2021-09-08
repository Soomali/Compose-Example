package com.mehmetali.dialogapp

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RecyclerViewAdapter(private val applicationContext: Context, private val initialSize:Int = 5):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    val items = mutableListOf<Bitmap>()
    class ViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView) {

        init {
            imageView.setImageResource(R.drawable.ic_launcher_background)
            //imageView.layoutParams.width = 600//items[p0]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ImageView(applicationContext,)
        view.layoutParams = ViewGroup.LayoutParams(

            ViewGroup.LayoutParams.MATCH_PARENT,
            800,
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.scaleType = ImageView.ScaleType.FIT_XY

        Glide.with(holder.imageView.context)
            .load("https://pbs.twimg.com/profile_images/1373073615026057216/CyF0UTd4_normal.jpg")
            .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = if(items.isEmpty()) initialSize else items.size
    /*private val items:MutableList<Bitmap> = mutableListOf()
    override fun getCount(): Int = if(items.isEmpty()) initialSize else items.size

    override fun getItem(p0: Int): Any =items[p0]

    override fun getItemId(p0: Int): Long = items[p0].hashCode().toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        if(p1 == null){
         val view = ImageView(applicationContext)
         view.setImageResource(R.drawable.ic_launcher_background)
            view.layoutParams.width = 600//items[p0]
         return view
        }
        return p1
    }*/
}