package com.example.epic_choice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic_choice.data.Product
import com.example.epic_choice.util.Resource
import com.google.api.Page
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
):ViewModel() {

    private val _epicPlaces = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicPlaces: StateFlow<Resource<List<Product>>> = _epicPlaces

    private val _epicDeals = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicDeals: StateFlow<Resource<List<Product>>> = _epicDeals

    private val _epixperiences = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epixperiences: StateFlow<Resource<List<Product>>> = _epixperiences

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts

    private val pagingInfo = PagingInfo()

    init {
        fetchEpicplaces()
        fetchEpicDeals()
        fetchEpixperiences()
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                firestore.collection("user").document(userId).collection("favorite")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("MainCategoryViewModel", "Error fetching favorites", exception)
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
                    Log.d("MaincategoryViewModel", "Product removed from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("MainCategoryViewModel", "Error removing product from favorites", e)
                }
        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(product)
                .addOnSuccessListener {
                    Log.d("MainCategoryViewModel", "Product added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("MainCategoryViewModel", "Error adding product to favorites", e)
                }
        }

    }

    fun fetchEpicplaces(){
        viewModelScope.launch {
            _epicPlaces.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Epic Places").get().addOnSuccessListener { result->
                val epicPlacesList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicPlaces.emit(Resource.Success(epicPlacesList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _epicPlaces.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchEpicDeals(){
        viewModelScope.launch {
            _epicDeals.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Epic Deals").get().addOnSuccessListener { result->
                val epicDealsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicDeals.emit(Resource.Success(epicDealsList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _epicDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchEpixperiences() {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _epixperiences.emit(Resource.Loading())
            }

            firestore.collection("Products")
                .limit(pagingInfo.epixperiencepage * 10)
                .get()
                .addOnSuccessListener { result ->
                    // Convert the result to a list of products
                    val allProducts = result.toObjects(Product::class.java)
                    Log.d("FetchEpixperiences", "Fetched ${allProducts.size} products.")

                    // Filter out unwanted categories
                    val filteredProducts = allProducts.filterNot {
                        it.category in listOf("Epic Deals", "Scenic Tours", "Epic Vehicles", "Hotels")
                    }
                    Log.d("FetchEpixperiences", "Filtered down to ${filteredProducts.size} products.")

                    // Handle UI updates on the main thread
                    viewModelScope.launch(Dispatchers.Main) {
                        pagingInfo.isPagingEnd = filteredProducts.isEmpty()
                        if (!pagingInfo.isPagingEnd) {
                            pagingInfo.oldepixperiencepage = filteredProducts
                            pagingInfo.epixperiencepage++
                        }
                        _epixperiences.emit(Resource.Success(filteredProducts))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FetchEpixperiences", "Failed to fetch products", exception)
                    viewModelScope.launch {
                        _epixperiences.emit(Resource.Error(exception.message.toString()))
                    }
                }
        }
    }



}

internal data class PagingInfo(
    var epixperiencepage: Long = 1,
    var oldepixperiencepage: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)



