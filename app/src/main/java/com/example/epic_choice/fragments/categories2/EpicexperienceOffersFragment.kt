package com.example.epic_choice.fragments.categories2


class EpicexperienceOffersFragment: BaseForAllFragment() {
    override val categories: List<String>
        get() = listOf("Activities")

    override val city: String?
        get() = null

    override val hasOffers: Boolean
        get() = true
}