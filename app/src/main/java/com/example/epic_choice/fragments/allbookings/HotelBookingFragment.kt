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
import java.util.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HotelBookingFragment : Fragment() {
    private val args: HotelBookingFragmentArgs by navArgs()

    private lateinit var hotelImageView: ImageView
    private lateinit var hotelNameTextView: TextView
    private lateinit var hotelPriceTextView: TextView
    private lateinit var checkInDateButton: Button
    private lateinit var checkOutDateButton: Button
    private lateinit var checkInDateTextView: TextView
    private lateinit var checkOutDateTextView: TextView
    private lateinit var singleRoomCountTextView: TextView
    private lateinit var doubleRoomCountTextView: TextView
    private lateinit var kingRoomCountTextView: TextView
    private lateinit var queenRoomCountTextView: TextView
    private lateinit var deluxeRoomCountTextView: TextView
    private lateinit var familyRoomCountTextView: TextView
    private lateinit var singleRoomPriceTextView: TextView
    private lateinit var doubleRoomPriceTextView: TextView
    private lateinit var kingRoomPriceTextView: TextView
    private lateinit var queenRoomPriceTextView: TextView
    private lateinit var deluxeRoomPriceTextView: TextView
    private lateinit var familyRoomPriceTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalRoomsTextView: TextView
    private lateinit var confirmBookingButton: Button
    private lateinit var backBookingButton: Button

    private var checkInDate: String? = null
    private var checkOutDate: String? = null
    private var price: Float? = null
    private var totalPrice: Float = 0f

    private var singleRoomCount = 0
    private var doubleRoomCount = 0
    private var kingRoomCount = 0
    private var queenRoomCount = 0
    private var deluxeRoomCount = 0
    private var familyRoomCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hotelbooking, container, false)

        // Initialize views
        initializeViews(view)

        val product = args.product
        hotelNameTextView.text = product.name
        price = if (product.offerPercentage != null) {
            val remainingPricePercentage = 1f - product.offerPercentage
            val priceAfterTheOffer = remainingPricePercentage * product.price!!
            hotelPriceTextView.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
            priceAfterTheOffer
        } else {
            hotelPriceTextView.text = "$ ${String.format("%.2f", product.price)}"
            product.price
        }

        // Load hotel image
        Glide.with(requireContext()).load(product.images[0]).into(hotelImageView)

        // Set up listeners for date pickers
        checkInDateButton.setOnClickListener { showDatePickerDialog(true) }
        checkOutDateButton.setOnClickListener { showDatePickerDialog(false) }

        // Set up room prices
        setRoomPrices()

        // Set up buttons to increment and decrement room counts
        setRoomButtonListeners(view)

        confirmBookingButton.setOnClickListener {
            if (isValidBooking()) {
                // Navigate and pass the total price
                val action = HotelBookingFragmentDirections
                    .actionHotelBookingFragmentToPaymentFragment(totalAmount = totalPrice)
                findNavController().navigate(action)
            } else {
                // Show error
                Toast.makeText(
                    context,
                    "Please select valid check-in and check-out dates and ensure at least one room is selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        backBookingButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun initializeViews(view: View) {
        hotelImageView = view.findViewById(R.id.hotelimgView)
        hotelNameTextView = view.findViewById(R.id.textHotelName)
        hotelPriceTextView = view.findViewById(R.id.textHotelPrice)
        checkInDateButton = view.findViewById(R.id.buttonCheckInDate)
        checkOutDateButton = view.findViewById(R.id.buttonCheckOutDate)
        checkInDateTextView = view.findViewById(R.id.textCheckInDate)
        checkOutDateTextView = view.findViewById(R.id.textCheckOutDate)
        singleRoomCountTextView = view.findViewById(R.id.textsingleRoomCount)
        doubleRoomCountTextView = view.findViewById(R.id.textDoubleRoomCount)
        kingRoomCountTextView = view.findViewById(R.id.textKingRoomCount)
        queenRoomCountTextView = view.findViewById(R.id.textQueenRoomCount)
        deluxeRoomCountTextView = view.findViewById(R.id.textDeluxeRoomCount)
        familyRoomCountTextView = view.findViewById(R.id.textFamilyRoomCount)
        singleRoomPriceTextView = view.findViewById(R.id.textsingleRoomprice)
        doubleRoomPriceTextView = view.findViewById(R.id.textdblRoomprice)
        kingRoomPriceTextView = view.findViewById(R.id.textkingRoomprice)
        queenRoomPriceTextView = view.findViewById(R.id.textqueenRoomprice)
        deluxeRoomPriceTextView = view.findViewById(R.id.textdeluxeRoomprice)
        familyRoomPriceTextView = view.findViewById(R.id.textfamilyRoomprice)
        totalPriceTextView = view.findViewById(R.id.textTotalPrice)
        totalRoomsTextView = view.findViewById(R.id.textTotalRooms)
        confirmBookingButton = view.findViewById(R.id.confirmBookingButton)
        backBookingButton = view.findViewById(R.id.backBookingButton)
    }

    private fun showDatePickerDialog(isCheckIn: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                if (isCheckIn) {
                    if (checkOutDate != null && !isDateAfter(date, checkOutDate!!)) {
                        // Prevent setting check-in date if it's after check-out date
                        Toast.makeText(
                            context,
                            "Check-in date cannot be after check-out date",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@DatePickerDialog
                    }
                    checkInDate = date
                    checkInDateTextView.text = checkInDate
                } else {
                    if (checkInDate != null && !isDateAfter(date, checkInDate!!)) {
                        // Prevent setting check-out date if it's before or same as check-in date
                        Toast.makeText(
                            context,
                            "Check-out date cannot be before or same as check-in date",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@DatePickerDialog
                    }
                    checkOutDate = date
                    checkOutDateTextView.text = checkOutDate
                }
                updateTotalPrice()
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun isDateAfter(date1: String, date2: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val d1 = dateFormat.parse(date1)
        val d2 = dateFormat.parse(date2)
        return d1 != null && d2 != null && d1.after(d2)
    }

    private fun setRoomPrices() {
        singleRoomPriceTextView.text = "$ ${String.format("%.2f", (price ?: 0f) * 0.5f)}"
        doubleRoomPriceTextView.text = "$ ${String.format("%.2f", (price ?: 0f))}"
        kingRoomPriceTextView.text = "$ ${String.format("%.2f", (price ?: 0f) * 1.4f)}"
        queenRoomPriceTextView.text = "$ ${String.format("%.2f", (price ?: 0f) * 1.8f)}"
        deluxeRoomPriceTextView.text = "$ ${String.format("%.2f", (price ?: 0f) * 2.4f)}"
        familyRoomPriceTextView.text = "$ ${String.format("%.2f", (price ?: 0f) * 2.8f)}"
    }

    private fun setRoomButtonListeners(view: View) {
        // Single Room Buttons
        view.findViewById<Button>(R.id.buttonincSingleRoom).setOnClickListener {
            singleRoomCount++
            singleRoomCountTextView.text = singleRoomCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttondecSingleRoom).setOnClickListener {
            if (singleRoomCount > 0) singleRoomCount--
            singleRoomCountTextView.text = singleRoomCount.toString()
            updateTotalPrice()
        }

        // Double Room Buttons
        view.findViewById<Button>(R.id.buttonincDoubleRoom).setOnClickListener {
            doubleRoomCount++
            doubleRoomCountTextView.text = doubleRoomCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttondecDoubleRoom).setOnClickListener {
            if (doubleRoomCount > 0) doubleRoomCount--
            doubleRoomCountTextView.text = doubleRoomCount.toString()
            updateTotalPrice()
        }

        // King Room Buttons
        view.findViewById<Button>(R.id.buttonincKingRoom).setOnClickListener {
            kingRoomCount++
            kingRoomCountTextView.text = kingRoomCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttondecKingRoom).setOnClickListener {
            if (kingRoomCount > 0) kingRoomCount--
            kingRoomCountTextView.text = kingRoomCount.toString()
            updateTotalPrice()
        }

        // Queen Room Buttons
        view.findViewById<Button>(R.id.buttonincQueenRoom).setOnClickListener {
            queenRoomCount++
            queenRoomCountTextView.text = queenRoomCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttondecQueenRoom).setOnClickListener {
            if (queenRoomCount > 0) queenRoomCount--
            queenRoomCountTextView.text = queenRoomCount.toString()
            updateTotalPrice()
        }

        // Deluxe Room Buttons
        view.findViewById<Button>(R.id.buttonincDeluxeeRoom).setOnClickListener {
            deluxeRoomCount++
            deluxeRoomCountTextView.text = deluxeRoomCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttondecDeluxeRoom).setOnClickListener {
            if (deluxeRoomCount > 0) deluxeRoomCount--
            deluxeRoomCountTextView.text = deluxeRoomCount.toString()
            updateTotalPrice()
        }

        // Family Room Buttons
        view.findViewById<Button>(R.id.buttonincFamilyRoom).setOnClickListener {
            familyRoomCount++
            familyRoomCountTextView.text = familyRoomCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttondecFamilyRoom).setOnClickListener {
            if (familyRoomCount > 0) familyRoomCount--
            familyRoomCountTextView.text = familyRoomCount.toString()
            updateTotalPrice()
        }
    }

    private fun isValidBooking(): Boolean {
        return checkInDate != null &&
                checkOutDate != null &&
                !checkInDate.isNullOrBlank() &&
                !checkOutDate.isNullOrBlank() &&
                (singleRoomCount > 0 ||
                        doubleRoomCount > 0 ||
                        kingRoomCount > 0 ||
                        queenRoomCount > 0 ||
                        deluxeRoomCount > 0 ||
                        familyRoomCount > 0)
    }

    private fun updateTotalPrice() {
        // Calculate number of days
        val days = if (checkInDate != null && checkOutDate != null) {
            val checkInCalendar = Calendar.getInstance().apply {
                val parts = checkInDate!!.split("/")
                set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            }
            val checkOutCalendar = Calendar.getInstance().apply {
                val parts = checkOutDate!!.split("/")
                set(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
            }
            ((checkOutCalendar.timeInMillis - checkInCalendar.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        } else {
            1 // Default to 1 day if dates are not set
        }

        // Calculate prices for different room types
        val singleRoomPrice = (price ?: 0f) * 0.5f
        val doubleRoomPrice = (price ?: 0f)
        val kingRoomPrice = (price ?: 0f) * 1.4f
        val queenRoomPrice = (price ?: 0f) * 1.8f
        val deluxeRoomPrice = (price ?: 0f) * 2.4f
        val familyRoomPrice = (price ?: 0f) * 2.8f

        // Calculate the total price based on room count and days
        totalPrice = (singleRoomCount * singleRoomPrice +
                doubleRoomCount * doubleRoomPrice +
                kingRoomCount * kingRoomPrice +
                queenRoomCount * queenRoomPrice +
                deluxeRoomCount * deluxeRoomPrice +
                familyRoomCount * familyRoomPrice) * days

        totalPriceTextView.text = "$ ${String.format("%.2f", totalPrice)}"
        totalRoomsTextView.text = (singleRoomCount + doubleRoomCount + kingRoomCount + queenRoomCount + deluxeRoomCount + familyRoomCount).toString()
    }
}