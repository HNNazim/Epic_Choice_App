package com.example.epic_choice.fragments.categories2

class AnurapuraHotelsFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Stays")

    override val city: String?
        get() = "Anuradhapura"

    override val hasOffers: Boolean
        get() = false
}