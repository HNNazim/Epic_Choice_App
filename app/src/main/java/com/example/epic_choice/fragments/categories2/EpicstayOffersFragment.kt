package com.example.epic_choice.fragments.categories2


class EpicstayOffersFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Stays")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = true
}