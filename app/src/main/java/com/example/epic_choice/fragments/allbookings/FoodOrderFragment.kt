package com.example.epic_choice.fragments.allbookings

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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

class FoodOrderFragment : Fragment() {

    private val args: FoodOrderFragmentArgs by navArgs()

    private var totalPrice: Float = 0f

    private lateinit var foodImageView: ImageView
    private lateinit var foodNameTextView: TextView
    private lateinit var foodPriceTextView: TextView
    private lateinit var orderDateButton: Button
    private lateinit var orderDateTextView: TextView
    private lateinit var pizzaCountTextView: TextView
    private lateinit var burgerCountTextView: TextView
    private lateinit var saladCountTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var confirmOrderButton: Button
    private lateinit var backOrderButton: Button

    private var orderDate: String = ""
    private var pizzaCount = 0
    private var burgerCount = 0
    private var saladCount = 0


    private val pizzaPrice = 60.00f
    private val burgerPrice = 40.00f
    private val saladPrice = 50.00f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_foodorder, container, false)

        // Initialize views
        foodImageView = view.findViewById(R.id.ItemImageView)
        foodNameTextView = view.findViewById(R.id.textFoodName)
        foodPriceTextView = view.findViewById(R.id.textFoodPrice)
        orderDateButton = view.findViewById(R.id.buttonOrderDate)
        orderDateTextView = view.findViewById(R.id.textOrderDate)
        pizzaCountTextView = view.findViewById(R.id.textPizzaCount)
        burgerCountTextView = view.findViewById(R.id.textBurgerCount)
        saladCountTextView = view.findViewById(R.id.textSaladCount)
        totalPriceTextView = view.findViewById(R.id.textTotalPrice)
        confirmOrderButton = view.findViewById(R.id.confirmOrderButton)
        backOrderButton = view.findViewById(R.id.backOrderButton)

        val product = args.product

        // Ensure product is not null
        if (product == null) {
            Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            return view
        }

        foodNameTextView.text = product.name
        if (product.offerPercentage != null) {
            foodPriceTextView.text = String.format("%.0f%% OFF", product.offerPercentage * 100)
        } else {
            foodPriceTextView.visibility = View.INVISIBLE
        }

        Glide.with(requireContext()).load(product.images[0]).into(foodImageView)

        // Set up listeners for date picker
        orderDateButton.setOnClickListener { showDatePickerDialog() }

        // Set up buttons for quantity adjustment
        setFoodQuantityButtonListeners(view)

        confirmOrderButton.setOnClickListener {
            if (orderDate.isNotEmpty() && (pizzaCount > 0 || burgerCount > 0 || saladCount > 0)) {
                val action = FoodOrderFragmentDirections.actionFoodOrderFragmentToPayAndDeliverFragment(totalAmount = totalPrice)
                findNavController().navigate(action)
            } else {
                // Show error
                Toast.makeText(context, "Please select a valid order date and quantity", Toast.LENGTH_SHORT).show()
            }
        }

        backOrderButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            orderDate = date
            orderDateTextView.text = "Order Date: $orderDate"
            updateTotalPrice()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun setFoodQuantityButtonListeners(view: View) {
        // Pizza Buttons
        view.findViewById<Button>(R.id.buttonIncPizza)?.setOnClickListener {
            pizzaCount++
            pizzaCountTextView.text = pizzaCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttonDecPizza)?.setOnClickListener {
            if (pizzaCount > 0) pizzaCount--
            pizzaCountTextView.text = pizzaCount.toString()
            updateTotalPrice()
        }

        // Burger Buttons
        view.findViewById<Button>(R.id.buttonIncBurger)?.setOnClickListener {
            burgerCount++
            burgerCountTextView.text = burgerCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttonDecBurger)?.setOnClickListener {
            if (burgerCount > 0) burgerCount--
            burgerCountTextView.text = burgerCount.toString()
            updateTotalPrice()
        }

        // Salad Buttons
        view.findViewById<Button>(R.id.buttonIncSalad)?.setOnClickListener {
            saladCount++
            saladCountTextView.text = saladCount.toString()
            updateTotalPrice()
        }
        view.findViewById<Button>(R.id.buttonDecSalad)?.setOnClickListener {
            if (saladCount > 0) saladCount--
            saladCountTextView.text = saladCount.toString()
            updateTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        val pizzaTotal: Float = pizzaPrice * pizzaCount
        val burgerTotal: Float = burgerPrice * burgerCount
        val saladTotal: Float = saladPrice * saladCount
        totalPrice = pizzaTotal + burgerTotal + saladTotal

        val offerPercentage: Float = args.product.offerPercentage?.toFloat() ?: 0.0f

        if (offerPercentage > 0) {
            val discount: Float = totalPrice * offerPercentage
            totalPrice -= discount
            totalPriceTextView.text = "Total Price: $ ${String.format("%.2f", totalPrice)} (Discount applied: ${String.format("%.2f",offerPercentage * 100)}%)"
        } else {
            totalPriceTextView.text = "Total Price: $ ${String.format("%.2f", totalPrice)}"
        }
        Log.d("FoodOrderFragment", "Total Price before navigation: $totalPrice")
    }


}