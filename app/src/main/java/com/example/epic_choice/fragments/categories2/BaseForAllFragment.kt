package com.example.epic_choice.fragments.categories2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.epic_choice.R
import com.example.epic_choice.adapters.BaseForAllDialogAdapter
import com.example.epic_choice.data.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseForAllFragment : DialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var baseForAllAdapter: BaseForAllDialogAdapter

    abstract val categories: List<String>
    abstract val city: String?
    abstract val hasOffers: Boolean

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_forall_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.RVBaseforAll)

        // Initialize adapter and set layout manager
        baseForAllAdapter = BaseForAllDialogAdapter()
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = baseForAllAdapter

        // Fetch and display products
        fetchProducts()

        // Handle product click to navigate to detailed view
        baseForAllAdapter.onClick = { product ->
            val bundle = Bundle().apply { putParcelable("product", product) }
            dismiss()
            findNavController().navigate(R.id.action_homeFragment_to_seeMoreFragment, bundle)
        }

        // Handle favorite click
        baseForAllAdapter.onFavoriteClick = { product, isCurrentlyFavorited ->
            toggleFavorite(product, isCurrentlyFavorited) { isFavorited ->
                baseForAllAdapter.updateFavoriteStatus(product.id, isFavorited)
            }
        }

        // Back button action
        view.findViewById<View>(R.id.btnBackToPage).setOnClickListener {
            dismiss()
        }

        // Display product name if available
        val productName = arguments?.getString("name")
        view.findViewById<TextView>(R.id.TVName).text = productName
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (resources.displayMetrics.heightPixels * 0.5).toInt())
    }

    private fun fetchProducts() {
        val databaseRef = FirebaseFirestore.getInstance().collection("Products")
        val currentCity = city
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        databaseRef.get().addOnSuccessListener { querySnapshot ->
            val productList = mutableListOf<Product>()
            for (document in querySnapshot.documents) {
                val product = document.toObject(Product::class.java)
                if (product != null && categories.contains(product.category)) {
                    if (currentCity == null || product.description?.contains(currentCity, ignoreCase = true) == true) {
                        if (hasOffers && product.offerPercentage != null) {
                            productList.add(product)
                        } else if (!hasOffers) {
                            productList.add(product)
                        }
                    }
                }
            }

            // If the user is authenticated, fetch their favorites
            userId?.let {
                fetchFavorites(it, productList)
            } ?: updateUIWithProducts(productList)
        }.addOnFailureListener { exception ->
            Log.e("BaseForAllFragment", "Error fetching products", exception)
        }
    }

    private fun fetchFavorites(userId: String, products: List<Product>) {
        FirebaseFirestore.getInstance().collection("user")
            .document(userId).collection("favorite")
            .get()
            .addOnSuccessListener { favoriteSnapshot ->
                val favoriteIds = favoriteSnapshot.documents.map { it.id }
                products.forEach { product ->
                    product.isFavorited = favoriteIds.contains(product.id)
                }
                updateUIWithProducts(products)
            }
    }

    private fun updateUIWithProducts(products: List<Product>) {
        baseForAllAdapter.submitList(products.sortedBy { it.name })
    }

    private fun toggleFavorite(product: Product, isCurrentlyFavorited: Boolean, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val favoriteCollection = FirebaseFirestore.getInstance()
            .collection("user").document(userId).collection("favorite")

        if (isCurrentlyFavorited) {
            // Remove from favorites
            favoriteCollection.document(product.id).delete()
                .addOnSuccessListener {
                    callback(false)
                    fetchProducts() // Refresh products after removal
                }
                .addOnFailureListener { e ->
                    Log.e("BaseForAllFragment", "Error removing product from favorites", e)
                }
        } else {
            // Add to favorites
            favoriteCollection.document(product.id).set(mapOf("name" to product.name, "image" to product.images[0]))
                .addOnSuccessListener {
                    callback(true)
                    fetchProducts() // Refresh products after addition
                }
                .addOnFailureListener { e ->
                    Log.e("BaseForAllFragment", "Error adding product to favorites", e)
                }
        }
    }
}
