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
import com.example.epic_choice.databinding.FragmentEpicBuyBinding
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.EpicBuyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "EpicBuyFragment"
@AndroidEntryPoint
open class EpicBuyFragment: Fragment(R.layout.fragment_epic_buy) {
    private lateinit var binding: FragmentEpicBuyBinding
    private lateinit var bestProductsAdapter: EpicBestProductsAdapter
    private lateinit var epicOffersAdapter: EpicProductOffersAdapter
    private lateinit var allProductsAdapter: EpicAllProductsAdapter

    private val viewModel by viewModels<EpicBuyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpicBuyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBestProductsRV()
        setUpEpicOffersRV()
        setUpAllProductsRV()

        bestProductsAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicOffersAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        allProductsAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }


        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }

        epicOffersAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }

        allProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }



        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.bestProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        bestProductsAdapter.differ.submitList(it.data)

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
            viewModel.epicOffers.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicOffersAdapter.differ.submitList(it.data)

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
            viewModel.allProducts.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        allProductsAdapter.differ.submitList(it.data)

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


    private fun setUpAllProductsRV() {
        allProductsAdapter = EpicAllProductsAdapter()
        binding.RVallProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = allProductsAdapter
        }
    }

    private fun setUpEpicOffersRV() {
        epicOffersAdapter = EpicProductOffersAdapter()
        binding.RVepicOffers.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicOffersAdapter
        }
    }

    private fun setUpBestProductsRV() {
        bestProductsAdapter = EpicBestProductsAdapter()
        binding.RVbestProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestProductsAdapter
        }
    }


}