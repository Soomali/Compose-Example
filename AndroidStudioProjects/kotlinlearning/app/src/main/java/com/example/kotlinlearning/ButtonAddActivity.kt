package com.example.kotlinlearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.example.kotlinlearning.databinding.ActivityButtonAddBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class ButtonAddActivity : AppCompatActivity() {
    private var lastId : Int = 0
    var data = ButtonData(null,onClick ={

        Log.d("[Button Add]",lastId.toString())
        print("")
    },"hey", width = LinearLayout.LayoutParams.WRAP_CONTENT,
        height = LinearLayout.LayoutParams.WRAP_CONTENT, layOut = 0)
        lateinit var adapter:ButtonAdapter
        lateinit var binding:ActivityButtonAddBinding
        lateinit var dbtalker : DbTalker
        private var todoitem:TodoItem = TodoItem(text = "heheheheheh",title = "first",date = Date(),id = null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button_add)
        dbtalker = DbTalker(applicationContext)
        binding = ActivityButtonAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ButtonAdapter()//applicationContext,android.R.layout.list_content)
        binding.listView.adapter = adapter
        binding.buttonAdderButton.setOnClickListener {
            createNextButton()
        }

    }
    private fun createNextButton(){

        val button =  ButtonReference(id=lastId,context = this,properties = data)
        button.button!!.setOnClickListener {
            Log.d("[Button ID]",it.id.toString())
            if(it.id == 5)  {dbtalker.insertTodoItem(todoitem)
                Log.d("[Todo Item Unapplied]",todoitem.date.toString())}
            if(it.id == 6){
                var liveData = dbtalker.getTodo(1)
                liveData.onChange =  { it1 ->
                    todoitem = it1?.get(0) ?: todoitem
                    var id:Int = it1?.get(0)?.id ?: -1
                    var text:String = if(it1 != null && it1.isNotEmpty()) it1[0].text
                    else "null"

                    Log.d("[Todo Item]",text)
                    Log.d("[Todo Item Id]",id.toString())
                    print("")
                }
            }
            if(it.id == 7) dbtalker.updateTodoItem(todoitem)
            if(it.id == 8) dbtalker.deleteTodoItem(todoitem)
            SharedSingleton.text += it.id.toString()
        }
        adapter.add(button)
        adapter.notifyDataSetChanged()
        lastId++
        data.text = lastId.toString()

    }
}