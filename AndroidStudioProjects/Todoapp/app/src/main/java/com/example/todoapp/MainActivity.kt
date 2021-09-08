package com.example.todoapp

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.GridView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.todoapp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private var lastId:Int = 2
    private lateinit var binding:ActivityMainBinding
    private lateinit var database:DbTalker
    private lateinit var adapter:GridAdapter
    @RequiresApi(Build.VERSION_CODES.N)
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
        val intent = result.data!!
        val id = intent.getIntExtra("id",-1)
        val title = intent.getStringExtra("title")!!
        val content = intent.getStringExtra("content")!!
        val todo = Todo(id=id,title=title,text=content)
        if(id == -1){
            database.insertTodoItem(todo)
            adapter.add(todo)
        }else{
            database.updateTodoItem(todo)
            adapter.update(todo)
        }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"))
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = DbTalker(applicationContext)
        adapter = GridAdapter(onDelete ={
            database.deleteTodoItem(it)
            null },binding.deleteCard,
            applicationContext,
            launcher = getContent)
        getTodoData()
        binding.gridView.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.gridView.verticalSpacing = 15
        binding.gridView.horizontalSpacing = 50
        binding.gridView.stretchMode = GridView.STRETCH_SPACING_UNIFORM

        binding.floatingActionButton.setOnClickListener{
            val intent = Intent(this,TodoItem::class.java)
            intent.putExtra("title","")
            intent.putExtra("content","")
            getContent.launch(intent)
        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTodoData(){
        database.getTodo(result = LiveDataHolder<List<Todo>>(
            onChange = {
                if(it != null){
                    adapter.addAll(it)
                }

            }
        ))
    }
}