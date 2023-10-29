package com.example.cigarettequitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CheckInitFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_init, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if there is data in the database using the shared view model
        lifecycleScope.launch {
            val initInfoCount = withContext(Dispatchers.IO) {
                sharedViewModel.database.initInfoDao().getInitInfoCount()
            }

            if (initInfoCount > 0) {
                // Database has data, navigate to HomeFragment
                findNavController().navigate(R.id.action_checkInitFragment_to_homeFragment)
            } else {
                // Database is empty, navigate to InitialFragment
                findNavController().navigate(R.id.action_checkInitFragment_to_initialFragment)
            }
        }

    }
}