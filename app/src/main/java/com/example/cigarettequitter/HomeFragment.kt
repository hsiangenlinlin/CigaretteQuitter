package com.example.cigarettequitter

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cigarettequitter.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDB: AppDatabase
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        appDB = sharedViewModel.database

        //create homeViewModel
        val viewModelFactory = HomeViewModelFactory(sharedViewModel.database.initInfoDao(), sharedViewModel.database.smokeCountDao())
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)


        // Observe the LiveData updates
        viewModel.name.observe(viewLifecycleOwner) { name ->
            binding.textViewHello.text = getString(R.string.welcome) + name + "!"
        }

        viewModel.quitDays.observe(viewLifecycleOwner) { quitDays ->
            binding.textViewDuration.text = getString(R.string.you_have_quit_for)+quitDays+ getString(
                            R.string.days)
        }

        viewModel.moneySaved.observe(viewLifecycleOwner) { savedMoney ->
            binding.textViewMoneySaved.text =  getString(R.string.you_have_saved) + savedMoney
        }

        viewModel.avoidCigarette.observe(viewLifecycleOwner) { avoidedAmount ->
            binding.textViewAvoidAmount.text = getString(R.string.you_have_avoided) + avoidedAmount + getString(
                            R.string.cigarettes)
        }

        viewModel.currentQuota.observe(viewLifecycleOwner) { currentQuota ->
            binding.textViewQuota.text = getString(R.string.quota_of_today) + currentQuota

        }
        binding.buttonSmokeOne.setOnClickListener {
            viewModel.currentQuota.value?.let { currentQuota ->
                if (currentQuota > 0) {
                    viewModel.decCurrentQuota()
                    viewModel.incTotalCigaretteSmoked()

                    Snackbar.make(view, getString(R.string.smoke_successfully), Snackbar.LENGTH_LONG).apply {
                        setAnchorView(binding.buttonSmokeOne) // Set the anchor view here
                        setAction(getString(R.string.undo)){
                            viewModel.incCurrentQuota()
                            viewModel.decTotalCigaretteSmoked()
                        }
                    }.show()

                } else {
                    Toast.makeText(requireContext(), getString(R.string.quota_finish), Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

