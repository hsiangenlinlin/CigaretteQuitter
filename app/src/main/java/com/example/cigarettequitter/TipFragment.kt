package com.example.cigarettequitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cigarettequitter.databinding.FragmentTipBinding

class TipFragment : Fragment() {
    private lateinit var appDB: AppDatabase
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentTipBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTipBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        appDB = sharedViewModel.database

        val viewModelFactory = TipsViewModelFactory(appDB.tipDao())
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(TipViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = TipAdapter()
        binding.tipsList.adapter = adapter

        viewModel.tips.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

