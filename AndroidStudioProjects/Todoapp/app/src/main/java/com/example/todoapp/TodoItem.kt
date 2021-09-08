package com.example.todoapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.todoapp.databinding.ActivityTodoItemBinding

class TodoItem : AppCompatActivity() {
    private lateinit var binding:ActivityTodoItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val id = intent.getIntExtra("id",-1)
        val database = DbTalker(applicationContext)

        binding = ActivityTodoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.contentEdit.setText(content)
        binding.titleEdit.setText(title)

        binding.floatingActionButton2.setOnClickListener {
            val text =  binding.titleEdit.text.toString()
            val contentText = binding.contentEdit.text.toString()
            if(text != "" && contentText != ""){
                Log.d("[Parameter Name]",text)
                Log.d("[Parameter Content]", contentText)
                val intent = Intent()
                intent.putExtra("id",id)
                intent.putExtra("title",text)
                intent.putExtra("content",contentText)
                setResult(Activity.RESULT_OK,intent)
                this.finish()
            }else{
                Toast.makeText(applicationContext, "isim ve içerik boş olamaz",
                    Toast.LENGTH_SHORT).show()
            }

        }
    }
}