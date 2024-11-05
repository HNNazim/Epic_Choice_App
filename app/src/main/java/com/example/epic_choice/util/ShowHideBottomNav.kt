package com.example.epic_choice.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.epic_choice.R
import com.example.epic_choice.activities.Next_Activity
import com.example.epic_choice.databinding.FragmentSeemoreBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavView(){
    val bottomNavigationView = (activity as Next_Activity).findViewById<BottomNavigationView>(
        R.id.BottomNavigation
    )
    bottomNavigationView.visibility = View.GONE

}

fun Fragment.showBottomNavView(){
    val bottomNavigationView = (activity as Next_Activity).findViewById<BottomNavigationView>(
        R.id.BottomNavigation
    )
    bottomNavigationView.visibility = View.VISIBLE

}