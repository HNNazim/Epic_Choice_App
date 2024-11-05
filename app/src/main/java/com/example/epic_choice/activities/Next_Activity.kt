package com.example.epic_choice.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.epic_choice.R
import com.example.epic_choice.databinding.NextActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Next_Activity: AppCompatActivity() {
    val binding by lazy{
        NextActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.HomePageFragment)
        binding.BottomNavigation.setupWithNavController(navController)
    }

}