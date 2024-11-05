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
import com.example.epic_choice.adapters.EpicBuy.EpicAllProductsAdapter
import com.example.epic_choice.adapters.EpicBuy.EpicBestProductsAdapter
import com.example.epic_choice.adapters.EpicBuy.EpicProductOffersAdapter
import com.example.epic_choice.adapters.EpicDine.EpicFoodTourAdapter
import com.example.epic_choice.adapters.EpicDine.EpicRestaurentsAdapter
import com.example.epic_choice.databinding.FragmentEpicDineBinding
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.EpicDineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "EpicDineFragment"
@AndroidEntryPoint
open class EpicDineFragment: Fragment(R.layout.fragment_epic_dine) {
    private lateinit var binding: FragmentEpicDineBinding
    private lateinit var foodToursAdapter: EpicFoodTourAdapter
    private lateinit var epicRestaurents: EpicRestaurentsAdapter

    private val viewModel by viewModels<EpicDineViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpicDineBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFoodToursRV()
        setUpRestaurentsRV()

        foodToursAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicRestaurents.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        foodToursAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }

        epicRestaurents.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }



        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.foodTours.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        foodToursAdapter.differ.submitList(it.data)

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
            viewModel.epicRestaurents.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicRestaurents.differ.submitList(it.data)

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


    private fun setUpRestaurentsRV() {
        epicRestaurents = EpicRestaurentsAdapter()
        binding.RVEpicRestaurents.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = epicRestaurents
        }
    }

    private fun setUpFoodToursRV() {
        foodToursAdapter = EpicFoodTourAdapter()
        binding.RVFoodTours.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = foodToursAdapter
        }
    }


}