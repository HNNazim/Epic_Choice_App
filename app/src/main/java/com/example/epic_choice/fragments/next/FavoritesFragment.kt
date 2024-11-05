package com.example.epic_choice.fragments.next

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic_choice.R
import com.example.epic_choice.adapters.FavoritesAdapter
import com.example.epic_choice.databinding.FragmentFavoritesBinding
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.FavoritesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "FavoritesFragment"

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: FavoritesAdapter

    private val viewModel by viewModels<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFavoritesRV()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Show loading state
                    }
                    is Resource.Success -> {
                        favoritesAdapter.submitList(resource.data)
                    }
                    is Resource.Error -> {
                        Log.e(TAG, resource.message.toString())
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Unspecified -> TODO()
                }
            }
        }
    }

    private fun setUpFavoritesRV() {
        favoritesAdapter = FavoritesAdapter()
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }
}
