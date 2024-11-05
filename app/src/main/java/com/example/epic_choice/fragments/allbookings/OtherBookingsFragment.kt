package com.example.epic_choice.fragments.allbookings

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.epic_choice.R
import java.util.Calendar

class OtherBookingsFragment : Fragment() {
    private val args: OtherBookingsFragmentArgs by navArgs()

    private lateinit var toursNameTextView: TextView
    private lateinit var toursPriceTextView: TextView
    private lateinit var selectDateTextView: TextView
    private lateinit var selectDateButton: Button
    private lateinit var confirmBookingButton: Button
    private lateinit var increasePeopleButton: Button
    private lateinit var decreasePeopleButton: Button
    private lateinit var toursImageView: ImageView
    private lateinit var backButton: Button
    private lateinit var totalPriceTextView: TextView
    private lateinit var peopleCountTextView: TextView

    private var selectedDate: Long? = null
    private var pricePerPerson: Float = 0f
    private var peopleCount: Int = 1
    private var totalPrice: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_otherbookings, container, false)

        // Initialize views
        toursNameTextView = view.findViewById(R.id.tourNameTextView)
        toursPriceTextView = view.findViewById(R.id.pricePerPersonTextView)
        selectDateTextView = view.findViewById(R.id.dateSelectTextView)
        selectDateButton = view.findViewById(R.id.dateSelectButton)
        peopleCountTextView = view.findViewById(R.id.peopleCountTextView)
        confirmBookingButton = view.findViewById(R.id.confirmBookingButton)
        increasePeopleButton = view.findViewById(R.id.increasePeopleButton)
        decreasePeopleButton = view.findViewById(R.id.decreasePeopleButton)
        toursImageView = view.findViewById(R.id.imgPacksview)
        backButton = view.findViewById(R.id.backBookingButton)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)

        // Retrieve the product from arguments
        val product = args.product

        // Display the first image from the list
        Glide.with(requireContext()).load(product.images[0]).into(toursImageView)

        // Set tour name and price per person
        toursNameTextView.text = product.name
        pricePerPerson = product.price ?: 0f // Default to 0f if price is null

        pricePerPerson = if (product.offerPercentage != null) {
            // Calculate the price after applying the offer
            val remainingPricePercentage = 1f - product.offerPercentage
            val priceAfterTheOffer = remainingPricePercentage * product.price!!
            toursPriceTextView.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
            priceAfterTheOffer
        } else {
            toursPriceTextView.text = "$ ${String.format("%.2f", product.price)}"
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

        // Handle date selection
        selectDateButton.setOnClickListener {
            openDatePicker()
        }

        confirmBookingButton.setOnClickListener {
            if (selectedDate == null) {
                Toast.makeText(requireContext(), "Please select a valid date", Toast.LENGTH_SHORT).show()
            } else {
                // Ensure totalPrice is up to date
                updateTotalPrice()

                // Navigate to PaymentFragment and pass the totalAmount
                val action = OtherBookingsFragmentDirections
                    .actionOtherBookingsFragmentToPaymentFragment(totalPrice)
                findNavController().navigate(action)
            }
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

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateMillis = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.timeInMillis

                val currentDateMillis = Calendar.getInstance().timeInMillis

                // Validate selected date
                if (selectedDateMillis < currentDateMillis) {
                    Toast.makeText(requireContext(), "Selected date cannot be in the past", Toast.LENGTH_SHORT).show()
                } else {
                    // Store the selected date and update UI
                    selectedDate = selectedDateMillis
                    selectDateTextView.text = "Date Picked: $selectedDay/${selectedMonth + 1}/$selectedYear"
                }
            },
            currentYear, currentMonth, currentDay
        )
        datePickerDialog.show()
    }
}
