package com.example.epic_choice.fragments.regLog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.activities.Next_Activity
import com.example.epic_choice.databinding.FragmentFirstpageBinding
import com.example.epic_choice.databinding.FragmentLoginBinding
import com.example.epic_choice.viewmodel.FirstPageViewModel
import com.example.epic_choice.viewmodel.FirstPageViewModel.Companion.NEXT_ACTIVITY
import com.example.epic_choice.viewmodel.FirstPageViewModel.Companion.REG_LOG_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class firstpagefragment : Fragment (R.layout.fragment_firstpage) {
    private lateinit var binding: FragmentFirstpageBinding
    private val viewModel by viewModels<FirstPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstpageBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.navigate.collect{
                when(it){
                    NEXT_ACTIVITY -> {
                        Intent(requireActivity(), Next_Activity::class.java).also{ intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                    REG_LOG_FRAGMENT ->{
                        findNavController().navigate(it)
                    }
                    else -> Unit
                }
            }
        }


        binding.BtnStart.setOnClickListener{
            viewModel.startButtonClick()
            findNavController().navigate(R.id.action_firstpagefragment2_to_reglogfragment2)
        }
    }
}