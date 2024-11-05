package com.example.epic_choice.fragments.allbookings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.epic_choice.R
import com.example.epic_choice.databinding.FragmentProductorderBinding

class ProductOrderFragment : Fragment(R.layout.fragment_productorder) {

    private lateinit var binding: FragmentProductorderBinding
    private val args: ProductOrderFragmentArgs by navArgs()

    private var totalPrice: Float = 0f
    private var itemCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductorderBinding.bind(view)

        val itemName = args.itemName
        val selectedColor = args.itemColor
        val itemImage = args.itemImage
        val itemPrice = args.itemPrice

        // Set selected color and item details
        binding.textItemName.text = itemName
        binding.imgColorSelect.setCircleBackgroundColor(parseColorOrDefault(selectedColor, "#FFFFFF"))
        Glide.with(this).load(itemImage).into(binding.ItemImageView)
        binding.textItemPrice.text = String.format("Price: $%.2f", itemPrice)

        // Initialize UI state
        updateItemCountAndPrice(itemCount, itemPrice)

        // Increment and decrement item count
        binding.buttonIncItem.setOnClickListener {
            itemCount++
            updateItemCountAndPrice(itemCount, itemPrice)
        }

        binding.buttonDecItem.setOnClickListener {
            if (itemCount > 0) {
                itemCount--
                updateItemCountAndPrice(itemCount, itemPrice)
            }
        }

        binding.buttonBackPayment.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonConfirmPayment.setOnClickListener {
            val action = ProductOrderFragmentDirections.actionProductOrderFragmentToPayAndDeliverFragment(totalAmount = totalPrice)
            findNavController().navigate(action)
        }
    }

    private fun updateItemCountAndPrice(count: Int, price: Float) {
        binding.tvItemCount.text = count.toString()
        totalPrice = (count * price)
        binding.textItemPrice.text = String.format("Total Price: $%.2f", totalPrice)

        // Disable the confirm button if count is zero
        binding.buttonConfirmPayment.isEnabled = count > 0
    }

    private fun parseColorOrDefault(colorString: String, defaultColor: String): Int {
        return try {
            Color.parseColor(colorString)
        } catch (e: IllegalArgumentException) {
            Color.parseColor(defaultColor)
        }
    }
}
