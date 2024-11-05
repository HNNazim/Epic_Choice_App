package com.example.epic_choice.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic_choice.R
import com.example.epic_choice.util.Constant.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstPageViewModel @Inject constructor(
   private val sharedPreferences: SharedPreferences,
   private val firebaseAuth: FirebaseAuth
)
: ViewModel(){
    
    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object{
        const val NEXT_ACTIVITY = 23
        val REG_LOG_FRAGMENT = R.id.action_firstpagefragment2_to_reglogfragment2
    }
    
    init {
    val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser
       
       if (user != null){
          viewModelScope.launch {
              _navigate.emit(NEXT_ACTIVITY)
          }
       }else if (isButtonClicked){
           viewModelScope.launch {
               _navigate.emit(REG_LOG_FRAGMENT)
           }
           
       }else{
          Unit
       }
    }

    fun startButtonClick(){
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY,true).apply()
    }
}