package com.example.kotlinlearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinlearning.databinding.ActivitySecondBinding




class Second : AppCompatActivity() {
    private val resolver:TextResolver = TextResolver("")
    private lateinit var binding:ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("[Start Activity]","here")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        resolver.text = intent.getStringExtra("text") ?: "yoh"
        binding =  ActivitySecondBinding.inflate(layoutInflater)
        binding.heyBensecond.text = resolver.text

        setContentView(binding.root)
        functionGiver()
        binding.heheheButton.setOnClickListener {
            binding.heyBensecond.text = resolver.restoreText()
            binding.heheheButton.textSize = 58f
        }
        binding.goBack.setOnClickListener {
            resolver.text = "'sadjw! 21 '!^se a"
            this.finish()
            
        }
    }
    private fun functionGiver() {

        val lists = listOf(binding.l2b1,binding.l2b2,binding.l2b3)
        for (i in lists){
            i.setOnClickListener {
                Log.d("[Assign Function]",i.text.toString())
                resolver.text += i.text.toString()
                Log.d("[Resolver Value]",resolver.text)
                binding.heyBensecond.text = resolver.text
                SharedSingleton.text += it.id

            }

        }


    }

}