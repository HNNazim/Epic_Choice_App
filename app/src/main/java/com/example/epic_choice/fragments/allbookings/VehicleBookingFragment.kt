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

class VehicleBookingFragment : Fragment() {
    private val args: VehicleBookingFragmentArgs by navArgs()

    private lateinit var vehicleNameTextView: TextView
    private lateinit var vehiclePriceTextView: TextView
    private lateinit var checkInDateTextView: TextView
    private lateinit var checkOutDateTextView: TextView
    private lateinit var selectCheckInDateButton: Button
    private lateinit var selectCheckOutDateButton: Button
    private lateinit var confirmBookingButton: Button
    private lateinit var vehicleImageView: ImageView
    private lateinit var backButton: Button
    private lateinit var totalPriceTextView: TextView

    private var checkInDate: Long? = null
    private var checkOutDate: Long? = null
    private var price: Float? = null
    private var totalPrice: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicles_booking, container, false)

        // Initialize views
        vehicleNameTextView = view.findViewById(R.id.vehicleNameTextView)
        vehiclePriceTextView = view.findViewById(R.id.vehiclePriceTextView)
        checkInDateTextView = view.findViewById(R.id.checkInDateTextView)
        checkOutDateTextView = view.findViewById(R.id.checkOutDateTextView)
        selectCheckInDateButton = view.findViewById(R.id.selectCheckInDateButton)
        selectCheckOutDateButton = view.findViewById(R.id.selectCheckOutDateButton)
        confirmBookingButton = view.findViewById(R.id.confirmBookingButton)
        vehicleImageView = view.findViewById(R.id.vehicleImageView)
        backButton = view.findViewById(R.id.backBookingButton)
        totalPriceTextView = view.findViewById(R.id.tvTotalPriceView)

        // Retrieve the product from arguments
        val product = args.product
        price = if (product.offerPercentage != null) {
            // Calculate the price after applying the offer
            val remainingPricePercentage = 1f - product.offerPercentage
            val priceAfterTheOffer = remainingPricePercentage * product.price!!
            vehiclePriceTextView.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
            priceAfterTheOffer
        } else {
            vehiclePriceTextView.text = "$ ${String.format("%.2f", product.price)}"
            product.price
        }

        // Display the first image from the list
        Glide.with(requireContext()).load(product.images[0]).into(vehicleImageView)

        // Set up date pickers
        selectCheckInDateButton.setOnClickListener { openDatePicker(true) }
        selectCheckOutDateButton.setOnClickListener { openDatePicker(false) }

        // Handle booking confirmation
        confirmBookingButton.setOnClickListener {
            if (checkInDate != null && checkOutDate != null && checkOutDate!! > checkInDate!!) {
                // Calculate total price and navigate to PaymentFragment
                val finalTotalPrice = calculateTotalPrice(price!!)
                findNavController().navigate(VehicleBookingFragmentDirections.actionVehicleBookingFragmentToPaymentFragment(
                    totalAmount = finalTotalPrice))
            } else {
                // Show error
                Toast.makeText(context, "Please select valid check-in and check-out dates", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun calculateTotalPrice(price: Float): Float {
        var totalPrice: Float = 0f
        if (checkInDate != null && checkOutDate != null) {
            // Calculate the number of days between check-in and check-out
            val noOfDays = (checkOutDate!! - checkInDate!!) / (1000 * 60 * 60 * 24)

            if (noOfDays > 0) {
                // Calculate and display the total price for the given number of days
                totalPrice = price * noOfDays
                totalPriceTextView.text = "$ ${String.format("%.2f", totalPrice)}"
            } else {
                totalPriceTextView.text = "Invalid dates"
            }
        } else {
            totalPriceTextView.text = "Select both dates"
        }
        return totalPrice
    }

    private fun openDatePicker(isCheckIn: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }.timeInMillis

                if (isCheckIn) {
                    checkInDate = selectedDate
                    checkInDateTextView.text = "Check-in: $selectedDay/${selectedMonth + 1}/$selectedYear"
                } else {
                    checkOutDate = selectedDate
                    checkOutDateTextView.text = "Check-out: $selectedDay/${selectedMonth + 1}/$selectedYear"
                }

                // Recalculate the total price whenever dates are selected
                price?.let { calculateTotalPrice(it) }
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
