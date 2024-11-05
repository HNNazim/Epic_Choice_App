package com.example.epic_choice.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic_choice.R
import com.example.epic_choice.adapters.EpicRide.EpicMapsAdapter
import com.example.epic_choice.adapters.EpicRide.EpicScenicTourAdapter
import com.example.epic_choice.adapters.EpicRide.EpicTourPacksAdapter
import com.example.epic_choice.adapters.EpicRide.EpicVehicleRentAdapter
import com.example.epic_choice.databinding.FragmentEpicRideBinding
import com.example.epic_choice.fragments.categories2.BicyclesFragment
import com.example.epic_choice.fragments.categories2.BikesFragment
import com.example.epic_choice.fragments.categories2.BusesFragment
import com.example.epic_choice.fragments.categories2.CarsFragment
import com.example.epic_choice.fragments.categories2.HelicopterToursFragment
import com.example.epic_choice.fragments.categories2.TrainToursFragment
import com.example.epic_choice.fragments.categories2.VansFragment
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.EpicRideViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "EpicRideFragment"
@AndroidEntryPoint
open class EpicRideFragment: Fragment(R.layout.fragment_epic_ride) {
    private lateinit var binding: FragmentEpicRideBinding
    private lateinit var epicVehicleRentAdapter: EpicVehicleRentAdapter
    private lateinit var epicScenicTourAdapter: EpicScenicTourAdapter
    private lateinit var epicMapsAdapter: EpicMapsAdapter
    private lateinit var epicTPackAdapter: EpicTourPacksAdapter

    private val viewModel by viewModels<EpicRideViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpicRideBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpVehicleRentsRV()
        setUpScenicToursRV()
        setUpEpicMapsRV()
        setUpEpicTPacksRV()

        epicMapsAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicTPackAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicMapsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_mapFragment, b)
        }

        epicTPackAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }


        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.vehicleRents.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicVehicleRentAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.scenicTours.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicScenicTourAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.epicMaps.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicMapsAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.epicTPacks.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicTPackAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun navigateToCategories(name: String) {
        val fragment = when (name) {
            "Epic Helicopter Tours" -> HelicopterToursFragment()
            "Epic Train Tours" -> TrainToursFragment()
            "Epic Bikes" -> BikesFragment()
            "Epic Bicycles" -> BicyclesFragment()
            "Epic Cars" -> CarsFragment()
            "Epic Vans" -> VansFragment()
            "Epic Buses" -> BusesFragment()

            else -> throw IllegalArgumentException("Unknown category: $name")
        }.apply {
            arguments = Bundle().apply {
                putString("name", name)
                // Optionally add more arguments here if needed
            }
        }

        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
        Log.d("EpicRideFragment", "Dialog Fragment shown for category: $name")
    }

    private fun setUpEpicTPacksRV() {
        epicTPackAdapter = EpicTourPacksAdapter()
        binding.RVTourPacks.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicTPackAdapter
        }
    }


    private fun setUpEpicMapsRV() {
        epicMapsAdapter = EpicMapsAdapter()
        binding.RVepicMaps.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicMapsAdapter
        }
    }

    private fun setUpScenicToursRV() {
        epicScenicTourAdapter = EpicScenicTourAdapter{ name ->
            navigateToCategories(name)
        }
        binding.RVScenicTours.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicScenicTourAdapter
        }
    }

    private fun setUpVehicleRentsRV() {
       epicVehicleRentAdapter =EpicVehicleRentAdapter{ name ->
           navigateToCategories(name)
       }
        binding.RVVehicleRents.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = epicVehicleRentAdapter
        }
    }


}