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
class EpicExperiencesViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _epicEvents = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicEvents: StateFlow<Resource<List<Product>>> = _epicEvents

    private val _epicActivities = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicActivities: StateFlow<Resource<List<Product>>> = _epicActivities

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts


    init {
        fetchEpicEvents()
        fetchEpicActivities()
        fetchFavorites()

    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                firestore.collection("user").document(userId).collection("favorite")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("EpicExpViewModel", "Error fetching favorites", exception)
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
                    Log.d("EpicExpViewModel", "Product removed from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicExpViewModel", "Error removing product from favorites", e)
                }
        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(product)
                .addOnSuccessListener {
                    Log.d("EpicExpViewModel", "Product added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicExpViewModel", "Error adding product to favorites", e)
                }
        }

    }

    private fun fetchEpicActivities() {
        viewModelScope.launch {
            _epicActivities.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Activities").get().addOnSuccessListener { result ->
                val epicAvtivitiesList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicActivities.emit(Resource.Success(epicAvtivitiesList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _epicActivities.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchEpicEvents() {
        viewModelScope.launch {
            _epicEvents.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Events").get().addOnSuccessListener { result ->
                val epicEventsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicEvents.emit(Resource.Success(epicEventsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _epicEvents.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
