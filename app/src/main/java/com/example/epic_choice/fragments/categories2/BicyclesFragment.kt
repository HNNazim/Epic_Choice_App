package com.example.epic_choice.fragments.categories2

class BicyclesFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Bicycles")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = false
}