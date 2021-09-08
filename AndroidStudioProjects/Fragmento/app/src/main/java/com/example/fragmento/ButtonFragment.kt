package com.example.fragmento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fragmento.databinding.FragmentButtonBinding



class ButtonFragment : Fragment() {
    private var _binding: FragmentButtonBinding? = null
    private val binding get() = _binding!!

    //data binding happens here. binding is what we call to interact with the layout.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("[Next Button]","next Button is being created")
        _binding = FragmentButtonBinding.inflate(inflater,container,false)
        return binding.root
    }
    //this function is called right after onCreateView. setting listeners and such should
    //be declared here.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("[Next Button]","next Button is  painted")
        Log.d("[Next Button Data]",binding.nextButton.left.toString() + " "+binding.nextButton.right.toString()
        +" "+binding.nextButton.top.toString() + " " + binding.nextButton.bottom.toString())
        binding.nextButton.setOnClickListener {
            Log.d("[Next Button]","next Button has been pressed")
            val act = ButtonFragmentDirections.actionButtonFragmentToFragmentSecond2()
            val nav = findNavController()
            nav.navigate(act)

        }
        binding.prevButton.setOnClickListener {
            Log.d("[Previous Button]","previous Button has been pressed")
        }

    }
    //called when this view destroyed. it will happen when the fragment no longer exists in screen.
    override fun onDestroyView() {
        Log.d("[Next Button]","FUCKEEEEEEEEEEEEEEEEEEEEEEEEEEEEd\n\nFUCCCCk")

        super.onDestroyView()
        _binding = null
    }

}