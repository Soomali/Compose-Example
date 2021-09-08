package com.example.todoapp

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.N)
class GridAdapter(private val onDelete:((Todo) -> Unit?),private val card:CardView, private val applicationContext: Context, private val items:MutableList<Todo> = mutableListOf(), private val launcher:ActivityResultLauncher<Intent>): BaseAdapter() {
    private val animClose = AnimationUtils.loadAnimation(applicationContext,R.anim.slide_up)
    private val animUp = AnimationUtils.loadAnimation(applicationContext,R.anim.anim)
    private val fadeIn = AnimationUtils.loadAnimation(applicationContext,R.anim.fade_in)
    private val fadeOut = AnimationUtils.loadAnimation(applicationContext,R.anim.fade_out)
    @RequiresApi(Build.VERSION_CODES.N)
    private val dragListener: View.OnDragListener = View.OnDragListener{ v, event ->
        when(event.action){

            DragEvent.ACTION_DRAG_STARTED -> {
                if(event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){

                    Log.d("[ClipData DragStarted]",event.clipData?.toString() ?: "none")
                    Log.d("[CardData DragStarted]",event.localState?.toString() ?:"null")
                    val todo = event.localState as  Pair<Todo,Int>
                    items.remove(todo.first)
                    notifyDataSetChanged()
                    card.visibility = View.VISIBLE
                    card.startAnimation(animUp)

                    true
                }
                else{
                    false
                }
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d("[Entered]",v.toString())
                Log.d("[Is same]",(v == card).toString())
                if( v == card){
                    (v as? CardView)?.setCardBackgroundColor(Color.GREEN)

                    v.invalidate()
                }
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                if(v == card){
                    (v as? CardView)?.setCardBackgroundColor(Color.BLACK)


                    v.invalidate()
                }
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text

                if(v == card){
                    (v as? CardView)?.setCardBackgroundColor(Color.BLACK)


                    v.invalidate()
                    val todo = event.localState as Pair<Todo,Int>
                    items.remove(todo.first)
                    notifyDataSetChanged()
                }
                true
            }
            DragEvent.ACTION_DRAG_ENDED ->{

                val todo = event.localState as Pair<Todo,Int>

                when(event.result){
                    true -> onDelete(todo.first)
                    else ->  if(!items.contains(todo.first)) items.add(todo.second,todo.first)

                }
                if(v == card){
                    (v as? CardView)?.setCardBackgroundColor(Color.BLACK)

                    v.invalidate()
                }
                card.visibility = View.INVISIBLE
                card.startAnimation(animClose)

                notifyDataSetChanged()

                true
            }
            else -> {
                // An unknown action type was received.
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
                false
            }

        }



    }
    init {
        card.setOnDragListener(dragListener)
    }
    override fun getCount(): Int {
        Log.d("[Invoke Function]","getCount -> ${items.size}")
        return items.size
    }
    override fun getItem(p0: Int): Any = items[p0]

    override fun getItemId(p0: Int): Long = (items[p0].id)!!.toLong()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        lateinit var view:View
        Log.d("[Invoke Function]","getView($p0,$p1,$p2)")
        val inflater = p2?.context?.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            view = inflater.inflate(R.layout.todo_item, p2,false)
            val nameView = view.findViewById<TextView>(R.id.name)
            val contentView = view.findViewById<TextView>(R.id.explanation)
            val card = view.findViewById<CardView>(R.id.card)
            nameView.text = items[p0].title
            contentView.text = items[p0].text
            card.setOnClickListener {
                Log.d(
                    "[Card $p0]",
                    "Card $p0 has clicked, expected values: name = ${items[p0].title}, content= ${items[p0].title}"
                )
                val todo = items[p0]
                val intent = Intent(applicationContext,TodoItem::class.java)
                intent.putExtra("id",todo.id)
                intent.putExtra("title",todo.title)
                intent.putExtra("content",todo.text)
                launcher.launch(intent)
            }
            card.setOnLongClickListener {
                val shadow = View.DragShadowBuilder(card)
                val item = ClipData.Item(card.tag as? CharSequence)
                val dragData = ClipData(
                    card.tag as? CharSequence,
                    arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                    item,
                )
                card.startDragAndDrop(
                    dragData,
                    shadow,
                    Pair<Todo,Int>(items[p0],p0),
                    0
                )

                true

            }
        card.setOnDragListener(dragListener)

            return view
    }
    fun add(todo:Todo){
        items.add(todo)
        notifyDataSetChanged()
    }
    fun addAll(todos:Collection<Todo>){
        items.addAll(0,todos)
        notifyDataSetChanged()
    }
    fun update(todo:Todo) {
        for (i in 0..items.size){
            val td = items[i]
            if(td.id == todo.id){
                items[i] = todo
                break
            }
        }

        notifyDataSetChanged()
    }
}

private fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
private inline fun <reified T>getBaseParent(view:View): T? {
    var parent = view.parent
    while(parent !is T && parent != null){
        parent = parent.parent
    }
    return parent as T
}