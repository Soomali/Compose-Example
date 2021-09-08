package com.example.whatsappclone.Story

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.whatsappclone.databinding.StoryFragmentBinding

class Story : Fragment() {

    companion object {
        fun newInstance() = Story()
    }

    private lateinit var viewModel: StoryViewModel
    private var _binding:StoryFragmentBinding? = null
    private val binding
    get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StoryViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoryFragmentBinding.inflate(layoutInflater)
        val adapter:StoryAdapter = StoryAdapter()
        binding!!.storyView.adapter = adapter
        viewModel.getStories().observe(viewLifecycleOwner,{
            adapter.addNews(it)
        })
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}