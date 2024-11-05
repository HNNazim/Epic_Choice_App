package com.example.epic_choice.fragments.allbookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.epic_choice.R

class TourPackBookingFragment : Fragment() {
    private val args: TourPackBookingFragmentArgs by navArgs()

    private lateinit var tourNameTextView: TextView
    private lateinit var pricePerPersonTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var peopleCountTextView: TextView
    private lateinit var increasePeopleButton: Button
    private lateinit var decreasePeopleButton: Button
    private lateinit var confirmBookingButton: Button
    private lateinit var backButton: Button
    private lateinit var tPacksImageView: ImageView

    private var pricePerPerson: Float = 0f
    private var peopleCount: Int = 1
    private var totalPrice: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tpackagebookings, container, false)

        // Initialize views
        tourNameTextView = view.findViewById(R.id.tourNameTextView)
        pricePerPersonTextView = view.findViewById(R.id.pricePerPersonTextView)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        peopleCountTextView = view.findViewById(R.id.peopleCountTextView)
        increasePeopleButton = view.findViewById(R.id.increasePeopleButton)
        decreasePeopleButton = view.findViewById(R.id.decreasePeopleButton)
        confirmBookingButton = view.findViewById(R.id.confirmBookingButton)
        backButton = view.findViewById(R.id.backBookingButton)
        tPacksImageView = view.findViewById(R.id.imgPacksview)

        // Get the tour package or activity details passed to this fragment
        val product = args.product

        // Display the first image from the list
        Glide.with(requireContext()).load(product.images[0]).into(tPacksImageView)

        // Set tour name and price per person
        tourNameTextView.text = product.name
        pricePerPerson = if (product.offerPercentage != null) {
            // Calculate the price after applying the offer
            val remainingPricePercentage = 1f - product.offerPercentage
            val priceAfterTheOffer = remainingPricePercentage * product.price!!
            pricePerPersonTextView.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
            priceAfterTheOffer
        } else {
            pricePerPersonTextView.text = "$ ${String.format("%.2f", product.price)}"
            product.price!!
        }

        // Display initial total price
        updateTotalPrice()

        // Handle people count adjustments
        increasePeopleButton.setOnClickListener {
            peopleCount++
            peopleCountTextView.text = peopleCount.toString()
            updateTotalPrice()
        }

        decreasePeopleButton.setOnClickListener {
            if (peopleCount > 1) {
                peopleCount--
                peopleCountTextView.text = peopleCount.toString()
                updateTotalPrice()
            }
        }

        // Handle booking confirmation and pass total price to PaymentFragment
        confirmBookingButton.setOnClickListener {
            val action = TourPackBookingFragmentDirections.actionTourPackBookingFragmentToPaymentFragment(
                totalAmount = totalPrice
            )
            findNavController().navigate(action)
        }

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }

    private fun updateTotalPrice() {
        totalPrice = pricePerPerson * peopleCount
        totalPriceTextView.text = "$ ${String.format("%.2f", totalPrice)}"
    }
}
