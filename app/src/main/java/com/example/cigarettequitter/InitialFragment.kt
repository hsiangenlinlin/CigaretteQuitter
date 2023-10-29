package com.example.cigarettequitter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cigarettequitter.databinding.FragmentInitialBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InitialFragment : Fragment() {
    private var _binding: FragmentInitialBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDB: AppDatabase
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentInitialBinding.inflate(inflater, container, false)
        val view = binding.root
        //getting array from string resource
        //appDB = AppDatabase.getDatabase(requireContext())
        // Create a shared ViewModel and get a reference to the AppDatabase object
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        appDB = sharedViewModel.database
        val durationOptions = resources.getStringArray(R.array.durationOptions)

        if (binding.spinnerDuration != null) {
            val adapter = ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item, durationOptions)
            binding.spinnerDuration.adapter = adapter

            binding.spinnerDuration.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(binding.root.context,
                        getString(R.string.selected_duration) + " " + durationOptions[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        binding.button.setOnClickListener {
            //go to next fragment only when writeData was done successfully
            writeData {
                findNavController().navigate(R.id.action_initialFragment_to_homeFragment)
            }
        }

        return view
    }

    private fun writeData(onSuccess: () -> Unit){
        //if all the blanks are filled->write data
        val name = binding.editTextTextName.text.toString()
        val numberOfCigarette = binding.editTextNumOfCigarette.text.toString()
        val price = binding.editTextPriceOfCigarette.text.toString()
        var duration=binding.spinnerDuration.selectedItem.toString()
        val startDate = System.currentTimeMillis()

        //switch duration to number of days accordingly
        when(duration){
            "2 weeks"->duration= "14"
            "3 weeks"->duration= "21"
            "1 month"->duration= "28"
            "2 months"->duration= "60"
            "3 months"->duration= "90"
            "2個星期"->duration= "14"
            "3個星期"->duration= "21"
            "1個月"->duration= "28"
            "2個月"->duration= "60"
            "3個月"->duration= "90"
        }

        if(binding.chipQuitDirectly.isChecked){
            duration="0"
        }
        //check all the info is filled and insert data to DB
        if (name.isNotEmpty()&&numberOfCigarette.isNotEmpty()&&price.isNotEmpty()&&duration.isNotEmpty()){
            val initInfo = InitInfo(
                name,
                price.toFloat(),
                numberOfCigarette.toFloat(),
                duration.toFloat(),
                startDate
            )
            GlobalScope.launch(Dispatchers.Main) {
                appDB.initInfoDao().insertAll(initInfo)
                onSuccess()
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}