package com.example.epic_choice.fragments.categories2


class EpicrideOffersFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Bikes","Bicycles","Cars","Vans","Buses","TTours","HTours","TourPacks")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = true
}