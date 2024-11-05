package com.example.epic_choice.fragments.categories2

class ColomboHotelsFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Stays")

    override val city: String?
        get() = "Colombo"

    override val hasOffers: Boolean
        get() = false
}