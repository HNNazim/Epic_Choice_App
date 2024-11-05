package com.example.epic_choice.fragments.categories2

class BusesFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Buses")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = false
}
