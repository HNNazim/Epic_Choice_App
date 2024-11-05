package com.example.epic_choice.fragments.categories2

class NuwaraEliyaHotelsFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Stays")


    override val city: String?
        get() = "Nuwara Eliya"

    override val hasOffers: Boolean
        get() = false
}