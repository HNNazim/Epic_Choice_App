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
import com.example.epic_choice.adapters.EpicDine.EpicFoodTourAdapter
import com.example.epic_choice.adapters.EpicDine.EpicRestaurentsAdapter
import com.example.epic_choice.adapters.EpicExperiences.EpicActivitiesAdapter
import com.example.epic_choice.adapters.EpicExperiences.EpicFesAndEventsAdapter
import com.example.epic_choice.databinding.FragmentEpicExperiencesBinding
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.EpicDineViewModel
import com.example.epic_choice.viewmodel.EpicExperiencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "EpicExperienceFragment"
@AndroidEntryPoint
open class EpicExperienceFragment: Fragment(R.layout.fragment_epic_experiences) {
    private lateinit var binding: FragmentEpicExperiencesBinding
    private lateinit var epicEventsAdapter: EpicFesAndEventsAdapter
    private lateinit var epicActivitiesAdapter: EpicActivitiesAdapter

    private val viewModel by viewModels<EpicExperiencesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpicExperiencesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEpicEventsRV()
        setUpEpicactivitiesRV()

        epicActivitiesAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicEventsAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }


        epicActivitiesAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }

        epicEventsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }



        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.epicEvents.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicEventsAdapter.differ.submitList(it.data)

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
            viewModel.epicActivities.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicActivitiesAdapter.differ.submitList(it.data)

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


    private fun setUpEpicactivitiesRV() {
        epicActivitiesAdapter = EpicActivitiesAdapter()
        binding.RVActivities.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = epicActivitiesAdapter
        }
    }

    private fun setUpEpicEventsRV() {
        epicEventsAdapter = EpicFesAndEventsAdapter()
        binding.RVEvents.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicEventsAdapter
        }
    }


}