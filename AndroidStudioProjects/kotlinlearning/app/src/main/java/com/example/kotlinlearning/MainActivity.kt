package com.example.kotlinlearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.kotlinlearning.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val resolver:TextResolver = TextResolver("")
    override fun onCreate(savedInstanceState: Bundle?) {

        SharedSingleton.instance
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resolver.text = binding.Text.text.toString()
        binding.button.setOnClickListener {
            resolver.resolveText()
            binding.Text.text = resolver.text

        }
        binding.sonrakiButton.setOnClickListener {
            val intent = Intent(this,Second::class.java).apply{
                putExtra("text",resolver.text)
            }
            startActivity(intent)
        }
        binding.sonrakiButton.text = SharedSingleton.text
        binding.digerButon.setOnClickListener {
            val intent = Intent(this,ButtonAddActivity::class.java)
            startActivity(intent)
        }

    }
}