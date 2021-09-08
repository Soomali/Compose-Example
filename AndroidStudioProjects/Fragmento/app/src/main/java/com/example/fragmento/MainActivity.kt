package com.example.fragmento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmento.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val navController = findNavController(R.id.fragmentContainerView)
        //print(navController)

    }
}