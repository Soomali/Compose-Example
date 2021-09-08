package com.example.whatsappclone.ui.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.whatsappclone.Call.CallFragment
import com.example.whatsappclone.R
import com.example.whatsappclone.Story.Story

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentActivity) :
    FragmentStateAdapter(fm) {
    var currentFragment:Fragment? = null
    override fun createFragment(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ConversationFragment (defined as a static inner class below).
        currentFragment =
         when(position){
            1 -> Story.newInstance()
            2 -> CallFragment.newInstance()
            else ->ConversationFragment.newInstance(position)
        }
        return currentFragment!!
    }


    override fun getItemCount(): Int {
        // Show 3 total pages.
        return 3
    }



}