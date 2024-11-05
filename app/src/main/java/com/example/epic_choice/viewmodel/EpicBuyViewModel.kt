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
class EpicBuyViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val _epicOffers = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicOffers: StateFlow<Resource<List<Product>>> = _epicOffers

    private val _allProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val allProducts: StateFlow<Resource<List<Product>>> = _allProducts

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts

    init {
        fetchFavorites()
        fetchBestProducts()
        fetchEpicOffers()
        fetchAllProducts()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                firestore.collection("user").document(userId).collection("favorite")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("EpicBuyViewModel", "Error fetching favorites", exception)
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
                    Log.d("EpicBuyViewModel", "Product removed from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicBuyViewModel", "Error removing product from favorites", e)
                }
        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(product)
                .addOnSuccessListener {
                    Log.d("EpicBuyViewModel", "Product added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicBuyViewModel", "Error adding product to favorites", e)
                }
        }
    }

    private fun fetchAllProducts() {
        viewModelScope.launch {
            _allProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Products").get().addOnSuccessListener { result ->
                val allProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _allProducts.emit(Resource.Success(allProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _allProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchEpicOffers() {
        viewModelScope.launch {
            _epicOffers.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Offers").get().addOnSuccessListener { result ->
                val epicOffersList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicOffers.emit(Resource.Success(epicOffersList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _epicOffers.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Epic Products").get().addOnSuccessListener { result ->
                val bestProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}
