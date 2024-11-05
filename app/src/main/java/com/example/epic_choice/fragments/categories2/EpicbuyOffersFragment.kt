package com.example.epic_choice.fragments.categories2


class EpicbuyOffersFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Offers")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = true
}