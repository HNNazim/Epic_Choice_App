package com.example.epic_choice.fragments.categories2

class KandyHotelsFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Stays")

    override val city: String?
        get() = "Kandy"

    override val hasOffers: Boolean
        get() = false
}