package com.example.epic_choice.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic_choice.data.Product
import com.example.epic_choice.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _favorites = MutableLiveData<Resource<List<Product>>>()
    val favorites: LiveData<Resource<List<Product>>> = _favorites

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        _favorites.value = Resource.Loading()
        val userId = auth.currentUser?.uid ?: return

        db.collection("user") // Ensure this matches your Firestore collection name
            .document(userId)
            .collection("favorite") // Ensure this matches your Firestore collection name
            .get()
            .addOnSuccessListener { result ->
                val productList = result.mapNotNull { document ->
                    document.toObject(Product::class.java)
                }
                _favorites.value = Resource.Success(productList)
            }
            .addOnFailureListener { exception ->
                _favorites.value = Resource.Error(exception.message.toString())
                Log.e("FavoritesViewModel", "Error fetching favorites", exception)
            }
    }

    fun toggleFavorite(product: Product, isCurrentlyFavorited: Boolean, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val favoriteCollection = db.collection("user").document(userId).collection("favorite")

            if (isCurrentlyFavorited) {
                // Remove from favorites
                favoriteCollection.document(product.id)
                    .delete()
                    .addOnSuccessListener {
                        fetchFavorites() // Refresh the list of favorites
                        callback(false) // Notify that the product is no longer favorited
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FavoritesViewModel", "Error removing product from favorites", exception)
                    }
            } else {
                // Add to favorites
                favoriteCollection.document(product.id)
                    .set(mapOf("name" to product.name, "image" to product.images[0]))  // Add any required fields here
                    .addOnSuccessListener {
                        fetchFavorites() // Refresh the list of favorites
                        callback(true) // Notify that the product has been favorited
                    }
                    .addOnFailureListener { e ->
                        Log.e("FavoritesViewModel", "Error adding product to favorites", e)
                    }
            }
        }
    }
}
