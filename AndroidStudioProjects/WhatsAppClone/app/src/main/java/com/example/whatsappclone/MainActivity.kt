package com.example.whatsappclone

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.whatsappclone.Call.CallFragment
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.example.whatsappclone.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    var callback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Log.d("ChangeData","$position")
            invalidateOptionsMenu()
        }
    }
    private lateinit var binding: ActivityMainBinding
    lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.registerOnPageChangeCallback(callback)
        //tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater

        Log.d("[currentItem]","${viewPager.currentItem}")
        when(viewPager.currentItem)
        {
            0 ->inflater.inflate(R.menu.appmenu, menu)
            1 -> inflater.inflate(R.menu.story_menu, menu)
            2 -> inflater.inflate(R.menu.call_menu, menu)
        }

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Handle item selection
        return when (item.itemId) {
            R.id.connectedDevices -> {
                Toast.makeText(applicationContext,"First pressed",Toast.LENGTH_SHORT)
                true
            }
            R.id.contactSettings -> {
                Toast.makeText(applicationContext,"Second pressed",Toast.LENGTH_SHORT)
                true
            }
            R.id.deleteCalls -> {
                Log.d("[Item press]","pressed deleteCalls")
                val frag = (viewPager.adapter as SectionsPagerAdapter).currentFragment as CallFragment


                    frag.adapter.clear()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(callback)
    }
}