package com.example.epic_choice.fragments.next

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic_choice.R
import com.example.epic_choice.adapters.AddressAdapter
import com.example.epic_choice.data.Address
import com.example.epic_choice.databinding.FragmentPaymentanddeliveryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PayAndDeliverFragment : Fragment() {

    private lateinit var binding: FragmentPaymentanddeliveryBinding
    private lateinit var addressList: List<Address> // Updated to Address type
    private var selectedAddress: Address? = null // Selected address variable
    private val minPaymentPercentage = 0.10 // Minimum payment threshold (10%)
    private var totalAmount: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalAmount = PayAndDeliverFragmentArgs.fromBundle(it).totalAmount
            Log.d("PayAndDeliverFragment", "Received totalAmount: $totalAmount")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentanddeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("PayAndDeliverFragment", "Total Amount in onViewCreated: $totalAmount")
        binding.tvTotalAmount.text = "Total Amount: $${String.format("%.2f", totalAmount)}"

        setupPaymentMethodSpinner()
        setupAddressRecyclerView()
        fetchAddressesFromFirestore()

        binding.buttonConfirmPayment.setOnClickListener {
            handlePayment()
            clearAllDetails()
        }

        binding.buttonBackPayment.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imgAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_payAndDeliverFragment_to_addressFragment)
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
        val paymentMethods = resources.getStringArray(R.array.payment_methods)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = adapter

        binding.spinnerPaymentMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMethod = paymentMethods[position]
                binding.cardDetailsLayout.visibility = if (selectedMethod == "Credit/Debit Card") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupAddressRecyclerView() {
        binding.recyclerViewAddresses.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val adapter = AddressAdapter { address ->
            // Handle address selection
            selectedAddress = address
            Toast.makeText(requireContext(), "Selected address: ${address.addressTitle}", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewAddresses.adapter = adapter
    }

    private fun fetchAddressesFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return // Get the current user ID
        val addressesRef = firestore.collection("user").document(userId).collection("address")

        addressesRef.get().addOnSuccessListener { result ->
            val addresses = result.mapNotNull { document ->
                document.toObject(Address::class.java) // Assuming Address class has Firestore mappings
            }
            updateAddressList(addresses)
        }.addOnFailureListener { exception ->
            Log.e("PayAndDeliverFragment", "Error fetching addresses: ", exception)
            Toast.makeText(requireContext(), "Error fetching addresses", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAddressList(addresses: List<Address>) {
        val adapter = binding.recyclerViewAddresses.adapter as AddressAdapter
        adapter.submitList(addresses)
    }

    private fun handlePayment() {
        val enteredAmountStr = binding.etAmountToPay.text.toString()

        if (enteredAmountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount to pay", Toast.LENGTH_SHORT).show()
            return
        }

        val enteredAmount = enteredAmountStr.toDoubleOrNull()
        if (enteredAmount == null || enteredAmount <= 0) {
            Toast.makeText(requireContext(), "Invalid amount entered", Toast.LENGTH_SHORT).show()
            return
        }

        val minimumAmountRequired = totalAmount * minPaymentPercentage
        if (enteredAmount < minimumAmountRequired) {
            Toast.makeText(requireContext(), "You must pay at least 10% of the total amount. Minimum required: $$minimumAmountRequired", Toast.LENGTH_LONG).show()
            return
        }

        // Ensure an address is selected
        if (selectedAddress == null) {
            Toast.makeText(requireContext(), "Please select an address", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedPaymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()

        if (selectedPaymentMethod == "Credit/Debit Card") {
            val cardNumber = binding.etCardNumber.text.toString()
            val expiryDate = binding.etExpiryDate.text.toString()
            val cvv = binding.etCvv.text.toString()

            // Validate card details
            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all card details", Toast.LENGTH_SHORT).show()
                return
            }

            // Process card payment
            processCardPayment(cardNumber, expiryDate, cvv, enteredAmount, selectedAddress.toString())
        } else {
            Toast.makeText(requireContext(), "Payment with $selectedPaymentMethod of $$enteredAmount selected for address $selectedAddress", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processCardPayment(cardNumber: String, expiryDate: String, cvv: String, amount: Double, address: String) {
        // Implement your card payment logic here
        Toast.makeText(requireContext(), "Processing card payment of $$amount for address $address...", Toast.LENGTH_SHORT).show()
    }
}
