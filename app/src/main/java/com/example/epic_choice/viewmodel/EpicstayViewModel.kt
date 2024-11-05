package com.example.epic_choice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic_choice.data.Product
import com.example.epic_choice.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpicstayViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _epicStay = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicStay: StateFlow<Resource<List<Product>>> = _epicStay

    init {
        fetchEpicStay()

    }



    private fun fetchEpicStay() {
        viewModelScope.launch {
            _epicStay.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Hotels").get().addOnSuccessListener { result ->
                val epicHotelsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicStay.emit(Resource.Success(epicHotelsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _epicStay.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
