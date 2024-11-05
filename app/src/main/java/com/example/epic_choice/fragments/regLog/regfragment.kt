package com.example.epic_choice.fragments.regLog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.epic_choice.data.user
import com.example.epic_choice.databinding.FragmentRegisterBinding
import com.example.epic_choice.viewmodel.registerviewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.util.RegisterValidation
import com.example.epic_choice.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "regfragment"
@AndroidEntryPoint
class regfragment : Fragment () {
    private lateinit var binding:FragmentRegisterBinding
    private val viewModel by viewModels<registerviewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       binding.regbuttonlogin.setOnClickListener{

            findNavController().navigate(R.id.action_regfragment2_to_loginfragment2)
        }

        binding.apply {
            ButtonRegister.setOnClickListener {
                val user = user(
                    ETFName.text.toString().trim(),
                    ETLName.text.toString().trim(),
                    ETEmailAddress.text.toString().trim()
                )
                val password = ETPassword.text.toString()
                viewModel.createAccountWithemailandPassword(user,password)
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.register.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.ButtonRegister.startAnimation()
                        }
                        is Resource.Success -> {
                            Log.d("test", it.data.toString())
                            binding.ButtonRegister.revertAnimation()
                        }
                        is Resource.Error -> {
                            Log.e(TAG,it.message.toString())
                            binding.ButtonRegister.revertAnimation()
                        }
                        else -> Unit
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validation.collect{validation ->
                if(validation.firstName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.ETFName.apply {
                            requestFocus()
                            error = validation.firstName.message
                        }
                    }
                }
                if(validation.lastName is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.ETLName.apply {
                            requestFocus()
                            error = validation.lastName.message
                        }
                    }
                }
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.ETEmailAddress.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.ETPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }

    }
}