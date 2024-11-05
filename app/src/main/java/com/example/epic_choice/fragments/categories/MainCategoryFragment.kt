package com.example.epic_choice.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic_choice.R
import com.example.epic_choice.adapters.HomePage.EpicDealsAdapter
import com.example.epic_choice.adapters.HomePage.EpicPlacesAdapter
import com.example.epic_choice.adapters.HomePage.EpixperiencesAdapter
import com.example.epic_choice.data.Product
import com.example.epic_choice.databinding.FragmentMainCategoryBinding
import com.example.epic_choice.fragments.categories2.EpicbuyOffersFragment
import com.example.epic_choice.fragments.categories2.EpicdineOffersFragment
import com.example.epic_choice.fragments.categories2.EpicexperienceOffersFragment
import com.example.epic_choice.fragments.categories2.EpicguideOffersFragment
import com.example.epic_choice.fragments.categories2.EpicrideOffersFragment
import com.example.epic_choice.fragments.categories2.EpicstayOffersFragment
import com.example.epic_choice.util.Resource
import com.example.epic_choice.util.showBottomNavView
import com.example.epic_choice.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var epicPlacesAdapter: EpicPlacesAdapter
    private lateinit var epicDealsAdapter: EpicDealsAdapter
    private lateinit var epixperiencesAdapter: EpixperiencesAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEpicPlacesRV()
        setUpEpicDealsRV()
        setUpEpixperiencesRV()

        epicPlacesAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epixperiencesAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            viewModel.toggleFavorite(product, isCurrentlyFavorited)
        }

        epicPlacesAdapter.onClick = {
            navigateToSeeMoreFragment(it)
        }

        epixperiencesAdapter.onClick = {
            navigateToSeeMoreFragment(it)
        }

        observeViewModel()
        setUpScrollListener()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.epicPlaces.collectLatest { resource: Resource<List<Product>> ->
                when (resource) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        epicPlacesAdapter.differ.submitList(resource.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        handleError(resource.message)
                    }
                    else -> hideLoading()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.epicDeals.collectLatest { resource: Resource<List<Product>> ->
                when (resource) {
                    is Resource.Loading -> showLoading()
                    is Resource.Success -> {
                        epicDealsAdapter.differ.submitList(resource.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        handleError(resource.message)
                    }
                    else -> hideLoading()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.epixperiences.collectLatest { resource: Resource<List<Product>> ->
                when (resource) {
                    is Resource.Loading -> binding.epixperiencesProgressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        epixperiencesAdapter.differ.submitList(resource.data)
                        binding.epixperiencesProgressBar.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        handleError(resource.message)
                        binding.epixperiencesProgressBar.visibility = View.GONE
                    }
                    else -> binding.epixperiencesProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun handleError(message: String?) {
        Log.e(TAG, message.orEmpty())
        Toast.makeText(requireContext(), message ?: "An error occurred", Toast.LENGTH_SHORT).show()
    }


    private fun setUpScrollListener() {
        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchEpixperiences()
            }
        })
    }

    private fun navigateToSeeMoreFragment(product: Product) {
        val bundle = Bundle().apply { putParcelable("product", product) }
        findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, bundle)
    }

    private fun navigateToOffers(name: String) {
        val fragment = when (name) {
            "Epic Guide Deals" -> EpicguideOffersFragment()
            "Epic Ride Deals" -> EpicrideOffersFragment()
            "Epic Stay Deals" -> EpicstayOffersFragment()
            "Epic Dine Deals" -> EpicdineOffersFragment()
            "Epic Experience Deals" -> EpicexperienceOffersFragment()
            "Epic Buy Deals" -> EpicbuyOffersFragment()
            else -> throw IllegalArgumentException("Unknown category: $name")
        }.apply {
            arguments = Bundle().apply {
                putString("name", name)
            }
        }

        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
        Log.d(TAG, "Dialog Fragment shown for category: $name")
    }

    private fun setUpEpixperiencesRV() {
        epixperiencesAdapter = EpixperiencesAdapter()
        binding.RVEpixperiences.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = epixperiencesAdapter
        }
    }

    private fun setUpEpicDealsRV() {
        epicDealsAdapter = EpicDealsAdapter { name ->
            navigateToOffers(name)
        }
        binding.RVEpicDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicDealsAdapter
        }
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setUpEpicPlacesRV() {
        epicPlacesAdapter = EpicPlacesAdapter()
        binding.RVEpicPlaces.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = epicPlacesAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavView()
    }
}
