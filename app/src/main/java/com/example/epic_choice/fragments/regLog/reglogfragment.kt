package com.example.epic_choice.fragments.regLog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.databinding.FragmentLoginBinding
import com.example.epic_choice.databinding.FragmentReglogBinding

class reglogfragment : Fragment (R.layout.fragment_reglog) {
    private lateinit var binding: FragmentReglogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReglogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnReg.setOnClickListener{
            findNavController().navigate(R.id.action_reglogfragment2_to_regfragment2)
        }

        binding.btnLog.setOnClickListener{
            findNavController().navigate(R.id.action_reglogfragment2_to_loginfragment2)
        }
    }
}