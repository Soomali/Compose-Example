package com.example.fragmento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fragmento.databinding.FragmentSecondBinding


class FragmentSecond : Fragment() {


    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    //data binding happens here. binding is what we call to interact with the layout.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater,container,false)
        return binding.root
    }
    //this function is called right after onCreateView. setting listeners and such should
    //be declared here.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            Log.d("[Fuck Button]","next Button has been pressed")
            val act = FragmentSecondDirections.actionFragmentSecondToButtonFragment2()
            val nav = findNavController()
            nav.navigate(act)

        }
    }
    //called when this view destroyed. it will happen when the fragment no longer exists in screen.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}