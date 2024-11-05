package com.example.epic_choice.fragments.categories2

class CarsFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Cars")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = false
}