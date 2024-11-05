package com.example.epic_choice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic_choice.data.Product
import com.example.epic_choice.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpicDineViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _foodTours = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val foodTours: StateFlow<Resource<List<Product>>> = _foodTours

    private val _epicRestaurents = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicRestaurents: StateFlow<Resource<List<Product>>> = _epicRestaurents

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts


    init {
        fetchFoodTours()
        fetchEpiRestaurents()
        fetchFavorites()

    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                firestore.collection("user").document(userId).collection("favorite")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("EpicDineViewModel", "Error fetching favorites", exception)
                            return@addSnapshotListener
                        }
                        val favorites = snapshot?.toObjects(Product::class.java) ?: emptyList()
                        _favoriteProducts.value = favorites
                    }
            }
        }
    }

    fun toggleFavorite(product: Product, isCurrentlyFavorited: Boolean) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val favoriteCollection = firestore.collection("user").document(userId).collection("favorite")

        if (isCurrentlyFavorited) {
            // Remove from favorites
            favoriteCollection.document(product.id).delete()
                .addOnSuccessListener {
                    Log.d("EpicDineViewModel", "Product removed from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicDineViewModel", "Error removing product from favorites", e)
                }
        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(product)
                .addOnSuccessListener {
                    Log.d("EpicDineViewModel", "Product added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicDineViewModel", "Error adding product to favorites", e)
                }
        }

    }

    private fun fetchEpiRestaurents() {
        viewModelScope.launch {
            _epicRestaurents.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Restaurents").get().addOnSuccessListener { result ->
                val restaurentsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicRestaurents.emit(Resource.Success(restaurentsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _epicRestaurents.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchFoodTours() {
        viewModelScope.launch {
            _foodTours.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Food Tours").get().addOnSuccessListener { result ->
                val foodToursList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _foodTours.emit(Resource.Success(foodToursList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _foodTours.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
