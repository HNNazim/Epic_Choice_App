package com.example.epic_choice.fragments.categories2


class EpicdineOffersFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Restaurents", "Food Tours")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = true
}