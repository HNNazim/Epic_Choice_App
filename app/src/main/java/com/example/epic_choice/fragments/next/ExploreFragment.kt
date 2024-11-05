package com.example.epic_choice.fragments.next

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.databinding.FragmentExploreBinding

class ExploreFragment : Fragment(R.layout.fragment_explore) {
    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backArrowimg.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }



}
