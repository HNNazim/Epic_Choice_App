package com.example.epic_choice.fragments.categories2

class GalleHotelsFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Stays")

    override val city: String?
        get() = "Galle"

    override val hasOffers: Boolean
        get() = false
}