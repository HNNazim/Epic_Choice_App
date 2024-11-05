package com.example.epic_choice.fragments.categories2

class BikesFragment : BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Bikes")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = false
}
