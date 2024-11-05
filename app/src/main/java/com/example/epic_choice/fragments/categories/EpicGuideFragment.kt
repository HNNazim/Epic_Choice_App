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
import com.example.epic_choice.adapters.EpicGuide.EpicSafetyTipsAdapter
import com.example.epic_choice.adapters.EpicGuide.EpicTourGuidesAdapter
import com.example.epic_choice.adapters.EpicGuide.EpicTranslatorsAdapter
import com.example.epic_choice.adapters.EpicGuide.PopularPlacesAdapter
import com.example.epic_choice.databinding.FragmentEpicGuideBinding
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.EpicGuideViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "EpicGuideFragment"
@AndroidEntryPoint
open class EpicGuideFragment:Fragment(R.layout.fragment_epic_guide) {
    private lateinit var binding: FragmentEpicGuideBinding
    private lateinit var popularPlacesAdapter: PopularPlacesAdapter
    private lateinit var epicTourGuidesAdapter: EpicTourGuidesAdapter
    private lateinit var epicTranslatorsAdapter: EpicTranslatorsAdapter
    private lateinit var epicSafetyTipsAdapter: EpicSafetyTipsAdapter

    private val viewModel by viewModels<EpicGuideViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpicGuideBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPopularPlacesRV()
        setUpTourGuidesRV()
        setUpTranslatorsRV()
        setUpSafetyTipsRV()

        popularPlacesAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicTourGuidesAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicTranslatorsAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicSafetyTipsAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicTranslatorsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_translatorFragment, b)
        }

        popularPlacesAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }

        epicSafetyTipsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }

        epicTourGuidesAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product", it) }
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, b)
        }


        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.popularPlaces.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        popularPlacesAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.tourGuides.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicTourGuidesAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.translatorTools.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicTranslatorsAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.safetyTips.collectLatest {
                when(it){
                    is Resource.Loading ->{

                    }
                    is Resource.Success ->{
                        epicSafetyTipsAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error ->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


    }

    private fun setUpSafetyTipsRV() {
        epicSafetyTipsAdapter = EpicSafetyTipsAdapter()
        binding.RVEpicsafetytips.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = epicSafetyTipsAdapter
        }
    }

    private fun setUpTranslatorsRV() {
        epicTranslatorsAdapter = EpicTranslatorsAdapter()
        binding.RVepicTranslators.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = epicTranslatorsAdapter
        }
    }

    private fun setUpTourGuidesRV() {
        epicTourGuidesAdapter = EpicTourGuidesAdapter()
        binding.RVEpicGuides.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = epicTourGuidesAdapter
        }
    }

    private fun setUpPopularPlacesRV() {
       popularPlacesAdapter = PopularPlacesAdapter()
        binding.RVPopularPlaces.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = popularPlacesAdapter
        }
    }


}