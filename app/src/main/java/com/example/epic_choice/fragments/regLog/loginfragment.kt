package com.example.epic_choice.fragments.regLog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.Snackbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.activities.Next_Activity
import com.example.epic_choice.databinding.FragmentLoginBinding
import com.example.epic_choice.databinding.FragmentRegisterBinding
import com.example.epic_choice.dialog.setupBottomSheetDialog
import com.example.epic_choice.util.Resource
import com.example.epic_choice.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar;




@AndroidEntryPoint
class loginfragment : Fragment (R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logButtonRegister.setOnClickListener{

            findNavController().navigate(R.id.action_loginfragment2_to_regfragment2)
        }

        binding.apply {
            ButtonLogin.setOnClickListener{
                val email = ETEmailAddressLogin.text.toString().trim()
                val password = ETPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
        }

        binding.TVForgotPassword.setOnClickListener{
            setupBottomSheetDialog {
                email ->
                viewModel.resetPassword(email)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.resetPassword.collect{
                when(it){
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        Snackbar.make(requireView(), "Reset link was sent to your E-mail.", Snackbar.LENGTH_LONG).show();
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(), "Error:${it.message}", Snackbar.LENGTH_LONG).show();
                    }
                    else -> Unit
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.login.collect{
                when(it){
                    is Resource.Loading -> {
                      binding.ButtonLogin.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.ButtonLogin.revertAnimation()
                        Intent(requireActivity(),Next_Activity::class.java).also{ intent ->
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)
                      }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        binding.ButtonLogin.revertAnimation()

                    }
                    else -> Unit
                }
            }
        }
    }
}