package com.example.epic_choice.viewmodel

import androidx.lifecycle.ViewModel
import com.example.epic_choice.data.user
import com.example.epic_choice.util.Constant.USER_COLLECTION
import com.example.epic_choice.util.RegisterFieldsState
import com.example.epic_choice.util.RegisterValidation
import com.example.epic_choice.util.Resource
import com.example.epic_choice.util.validateEmail
import com.example.epic_choice.util.validateFirstName
import com.example.epic_choice.util.validateLastName
import com.example.epic_choice.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class registerviewmodel  @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel()
{
    private val _register = MutableStateFlow<Resource<user>>(Resource.Unspecified())
    val register:Flow<Resource<user>> = _register

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithemailandPassword(user: user, password: String) {
        if (checkValidation(user, password)) {

            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
        else {
            val registerFieldsState =RegisterFieldsState(
                validateFirstName(user.firstName),
                validateLastName(user.lastName),
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun saveUserInfo(userUid: String, user:user) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(user: user, password: String):Boolean {

        val FNameValidation = validateFirstName(user.firstName)
        val LNameValidation = validateLastName(user.lastName)
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = FNameValidation is RegisterValidation.Success &&
                LNameValidation is RegisterValidation.Success &&
                emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success

        return shouldRegister
    }
}