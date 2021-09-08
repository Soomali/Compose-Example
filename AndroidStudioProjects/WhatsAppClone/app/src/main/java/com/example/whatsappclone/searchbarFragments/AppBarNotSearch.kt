package com.example.whatsappclone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.whatsappclone.databinding.FragmentAppBarNotSearchBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AppBarNotSearch.newInstance] factory method to
 * create an instance of this fragment.
 */
class AppBarNotSearch() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAppBarNotSearchBinding? = null
    val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("[State]","AppBarNotSearch.onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppBarNotSearchBinding.inflate(layoutInflater)
        val tabs: TabLayout = binding!!.tabs
        val activity:MainActivity = this.activity as MainActivity

        TabLayoutMediator(tabs, activity.viewPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "SOHBETLER"

                }
                1 -> {
                    tab.text = "DURUM"
                }
                2 -> {
                    tab.text = "ARAMALAR"
                }

            }
        }.attach()
        activity.setSupportActionBar(binding!!.toolbar)
        binding!!.buttonSearch.setOnClickListener {
            val act = AppBarNotSearchDirections.actionAppBarNotSearchToAppBarWithSearch()
            val nav = findNavController()
            nav.navigate(act)
        }
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppBarNotSearch.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppBarNotSearch().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}