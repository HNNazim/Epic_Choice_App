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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic_choice.R
import com.example.epic_choice.adapters.EpicStay.EpicStayAdapter
import com.example.epic_choice.databinding.FragmentEpicStayBinding
import com.example.epic_choice.fragments.categories2.AnurapuraHotelsFragment
import com.example.epic_choice.fragments.categories2.ColomboHotelsFragment
import com.example.epic_choice.fragments.categories2.GalleHotelsFragment
import com.example.epic_choice.fragments.categories2.KandyHotelsFragment
import com.example.epic_choice.fragments.categories2.NuwaraEliyaHotelsFragment
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.EpicstayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TAG = "EpicStayFragment"
@AndroidEntryPoint
open class EpicStayFragment: Fragment(R.layout.fragment_epic_stay) {
    private lateinit var binding: FragmentEpicStayBinding
    private lateinit var epicStayAdapter: EpicStayAdapter

    private val viewModel by viewModels<EpicstayViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpicStayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpHotelsRV()


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.epicStay.collectLatest {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        epicStayAdapter.differ.submitList(it.data)

                    }

                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }

    }

    private fun navigateToHotels(name: String) {
        val fragment = when (name) {
            "Anuradhapura Hotels" -> AnurapuraHotelsFragment()
            "Colombo Hotels" -> ColomboHotelsFragment()
            "Galle Hotels" -> GalleHotelsFragment()
            "Kandy Hotels" -> KandyHotelsFragment()
            "Nuwara Eliya Hotels" -> NuwaraEliyaHotelsFragment()
            else -> throw IllegalArgumentException("Unknown category: $name")

        }.apply {
            arguments = Bundle().apply {
                putString("name", name)
                // Optionally add more arguments here if needed
            }
        }

        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
        Log.d("MainCategoryFragment", "Dialog Fragment shown for category: $name")
    }


    private fun setUpHotelsRV() {
        epicStayAdapter = EpicStayAdapter{ name ->
            navigateToHotels(name)
        }
        binding.RVHotels.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = epicStayAdapter
        }
    }


}