package com.example.tabbed

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.tabbed.ui.main.SectionsPagerAdapter
import com.example.tabbed.databinding.ActivityMainBinding
import com.example.tabbed.ui.main.PlaceholderFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private val onTabChanged:TabLayout.OnTabSelectedListener = object:TabLayout.OnTabSelectedListener{
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if(tab!!.position < sectionsPagerAdapter.getLength()){
                (sectionsPagerAdapter.getFragmentAt(tab!!.position ) as? PlaceholderFragment)?.setIndex(sectionsPagerAdapter.data)
                sectionsPagerAdapter.data++
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            }

    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewPager: ViewPager2 = binding.viewPager
         sectionsPagerAdapter = SectionsPagerAdapter(this,viewPager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "TAB HEHEHE"

                }
                1 -> {
                    tab.text = "TAB HAHAHA"
                }
                2 -> {
                    tab.text = "TAB HÜHÜHÜHÜ"
                }

            }
            sectionsPagerAdapter.data += position + 1
        }.attach()
        tabs.addOnTabSelectedListener(this.onTabChanged)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}