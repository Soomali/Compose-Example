package com.example.whatsappclone.Call

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.whatsappclone.R
import com.example.whatsappclone.databinding.CallFragmentBinding

class CallFragment : Fragment() {

    companion object {
        fun newInstance() = CallFragment()
    }

    private lateinit var callViewModel: CallViewModel
    val adapter = CallAdapter()

    private var _binding:CallFragmentBinding? = null
    val binding
    get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CallFragmentBinding.inflate(layoutInflater)
        binding!!.callView.adapter = adapter
        callViewModel.getCalls().observe(viewLifecycleOwner,{
            adapter.addNews(it)
        })
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callViewModel = ViewModelProvider(this).get(CallViewModel::class.java)

    }

}