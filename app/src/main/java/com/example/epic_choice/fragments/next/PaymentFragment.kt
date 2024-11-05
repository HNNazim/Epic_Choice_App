package com.example.epic_choice.fragments.next

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.epic_choice.R
import com.example.epic_choice.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private var totalAmount: Float = 0.0f
    private val minPaymentPercentage = 0.10f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalAmount = PayAndDeliverFragmentArgs.fromBundle(it).totalAmount
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display total amount
        binding.tvTotalAmount.text = "Total Amount: $${String.format("%.2f", totalAmount)}"

        // Setup payment methods
        setupPaymentMethodSpinner()

        // Confirm Payment Button Click
        binding.buttonConfirmPayment.setOnClickListener {
            handlePayment()
            clearAllDetails()
        }

        binding.buttonBackPayment.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun clearAllDetails() {
        binding.etAmountToPay.text.clear()
        binding.etCardNumber.text.clear()
        binding.etExpiryDate.text.clear()
        binding.etCvv.text.clear()

        // Reset Spinner to first item
        binding.spinnerPaymentMethod.setSelection(0)


    }

    private fun setupPaymentMethodSpinner() {
        // Setup the payment method spinner (you can define the array in res/values/arrays.xml)
        val paymentMethods = resources.getStringArray(R.array.payment_methods)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = adapter

        // Handle payment method selection
        binding.spinnerPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMethod = paymentMethods[position]
                if (selectedMethod == "Credit/Debit Card") {
                    binding.cardDetailsLayout.visibility = View.VISIBLE
                } else {
                    binding.cardDetailsLayout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun handlePayment() {
        val enteredAmountStr = binding.etAmountToPay.text.toString()
        if (enteredAmountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount to pay", Toast.LENGTH_SHORT).show()
            return
        }

        val enteredAmount = enteredAmountStr.toFloatOrNull()
        if (enteredAmount == null) {
            Toast.makeText(requireContext(), "Invalid amount entered", Toast.LENGTH_SHORT).show()
            return
        }

        // Calculate minimum required amount (10%)
        val minimumAmountRequired = totalAmount * minPaymentPercentage
        if (enteredAmount < minimumAmountRequired) {
            Toast.makeText(requireContext(), "You must pay at least 10% of the total amount. Minimum required: $$minimumAmountRequired", Toast.LENGTH_LONG).show()
            return
        }

        val selectedPaymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()

        // Handle credit/debit card payment
        if (selectedPaymentMethod == "Credit/Debit Card") {
            val cardNumber = binding.etCardNumber.text.toString()
            val expiryDate = binding.etExpiryDate.text.toString()
            val cvv = binding.etCvv.text.toString()

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all card details", Toast.LENGTH_SHORT).show()
                return
            }

            // Proceed with card payment
            processCardPayment(cardNumber, expiryDate, cvv, enteredAmount)
        } else {
            // Handle other payment methods (e.g., PayPal, Cash on Delivery)
            Toast.makeText(requireContext(), "Payment with $selectedPaymentMethod of $$enteredAmount selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processCardPayment(cardNumber: String, expiryDate: String, cvv: String, amount:Float) {
        // Logic for processing card payment
        // You can integrate a payment gateway API here (e.g., Stripe, PayPal)
        Toast.makeText(requireContext(), "Processing card payment of $$amount...", Toast.LENGTH_SHORT).show()
    }
}
