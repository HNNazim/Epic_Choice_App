package com.example.epic_choice.fragments.categories2


class EpicguideOffersFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Tour Guides")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = true
}