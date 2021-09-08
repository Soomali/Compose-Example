package com.example.tabbed.ui.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.tabbed.R



/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentActivity,private val viewPager: ViewPager2) :
    FragmentStateAdapter(fm) {
    var data = 0
    private val fragments = mutableListOf<Fragment>()
    fun getFragmentAt(position:Int):Fragment = fragments[position]
    fun getLength() = fragments.size
    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Log.d("[Data]" , "$data")
        val frag = PlaceholderFragment.newInstance(data,viewPager)
        fragments.add(frag)
        data += 1
        Log.d("[Frags]","$fragments")

        return frag
    }


    override fun getItemCount(): Int {
        // Show 3 total pages.
        return 3
    }


}