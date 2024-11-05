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
class EpicRideViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _vehicleRents = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val vehicleRents: StateFlow<Resource<List<Product>>> = _vehicleRents

    private val _scenicTours = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val scenicTours: StateFlow<Resource<List<Product>>> = _scenicTours

    private val _epicMaps = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicMaps: StateFlow<Resource<List<Product>>> = _epicMaps

    private val _epicTPacks = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val epicTPacks: StateFlow<Resource<List<Product>>> = _epicTPacks

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts


    init {
        fetchVehicleRents()
        fetchScenicTours()
        fetchEpicMaps()
        fetchEpicTPacks()
        fetchFavorites()

    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                firestore.collection("user").document(userId).collection("favorite")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("EpicRideViewModel", "Error fetching favorites", exception)
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
                    Log.d("EpicRideViewModel", "Product removed from favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicRideViewModel", "Error removing product from favorites", e)
                }

        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(product)
                .addOnSuccessListener {
                    Log.d("EpicRideViewModel", "Product added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.e("EpicRideViewModel", "Error adding product to favorites", e)
                }
        }

    }

    private fun fetchEpicTPacks() {
        viewModelScope.launch {
            _epicTPacks.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "TourPacks").get().addOnSuccessListener { result->
                val epicMapsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicTPacks.emit(Resource.Success(epicMapsList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _epicTPacks.emit(Resource.Error(it.message.toString()))
                }
            }
    }


    private fun fetchEpicMaps() {
        viewModelScope.launch {
            _epicMaps.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Maps").get().addOnSuccessListener { result->
                val epicMapsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _epicMaps.emit(Resource.Success(epicMapsList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _epicMaps.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchScenicTours() {
        viewModelScope.launch {
            _scenicTours.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Scenic Tours").get().addOnSuccessListener { result->
                val scenicToursList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _scenicTours.emit(Resource.Success(scenicToursList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _scenicTours.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchVehicleRents() {
        viewModelScope.launch {
            _vehicleRents.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Epic Vehicles").get().addOnSuccessListener { result->
                val epicVehiclesList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _vehicleRents.emit(Resource.Success(epicVehiclesList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _vehicleRents.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}