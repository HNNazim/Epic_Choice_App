package com.example.epic_choice.util

import com.example.epic_choice.viewmodel.registerviewmodel

sealed class RegisterValidation(){
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldsState(
    val firstName: RegisterValidation,
    val lastName: RegisterValidation,
    val email: RegisterValidation,
    val password: RegisterValidation
)