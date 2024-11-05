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
class EpicGuideViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _popularPlaces = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val popularPlaces: StateFlow<Resource<List<Product>>> = _popularPlaces

    private val _tourGuides = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val tourGuides: StateFlow<Resource<List<Product>>> = _tourGuides

    private val _translatorTools = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val translatorTools: StateFlow<Resource<List<Product>>> = _translatorTools

    private val _safetyTips = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val safetyTips: StateFlow<Resource<List<Product>>> = _safetyTips

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts

    init {
        fetchPopularPlaces()
        fetchTourGuides()
        fetchTranslatorTools()
        fetchSafetyTips()
        fetchFavorites()

    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                firestore.collection("user").document(userId).collection("favorite")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("EpicGuideViewModel", "Error fetching favorites", exception)
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
                    Log.d("EpicGuideViewModel", "Product removed from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicGuideViewModel", "Error removing product from favorites", e)
                }
        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(product)
                .addOnSuccessListener {
                    Log.d("EpicGuideViewModel", "Product added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicGuideViewModel", "Error adding product to favorites", e)
                }
        }

    }


    private fun fetchSafetyTips() {
        viewModelScope.launch {
            _safetyTips.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Safety Tips").get().addOnSuccessListener { result->
                val safetyTipsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _safetyTips.emit(Resource.Success(safetyTipsList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _safetyTips.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchTranslatorTools() {
        viewModelScope.launch {
            _translatorTools.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Translators").get().addOnSuccessListener { result->
                val translatorsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _translatorTools.emit(Resource.Success(translatorsList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _translatorTools.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchTourGuides() {
        viewModelScope.launch {
            _tourGuides.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Tour Guides").get().addOnSuccessListener { result->
                val tourGuidesList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _tourGuides.emit(Resource.Success(tourGuidesList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _tourGuides.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchPopularPlaces() {
        viewModelScope.launch {
            _popularPlaces.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Epic Places").get().addOnSuccessListener { result->
                val epicPlacesList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _popularPlaces.emit(Resource.Success(epicPlacesList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _popularPlaces.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}