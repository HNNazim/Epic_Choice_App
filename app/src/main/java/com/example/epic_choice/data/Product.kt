package com.example.epic_choice.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Float? = null,
    val offerPercentage: Float? = null,
    val description: String? = null,
    val colors: List<Int>? = null, // Resource IDs for colors
    val sizes: List<String>? = null,
    val images: List<String>,
    var isFavorited: Boolean = false
): Parcelable {
    constructor() : this("0", "", "", images = emptyList())
}
