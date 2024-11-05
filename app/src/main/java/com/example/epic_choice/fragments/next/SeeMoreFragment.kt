package com.example.epic_choice.fragments.next

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic_choice.R
import com.example.epic_choice.adapters.ColorsAdapter
import com.example.epic_choice.adapters.SizesAdapter
import com.example.epic_choice.adapters.ViewPager2Imgs
import com.example.epic_choice.data.Product
import com.example.epic_choice.databinding.FragmentSeemoreBinding

class SeeMoreFragment : Fragment() {

    private val args by navArgs<SeeMoreFragmentArgs>()
    private lateinit var binding: FragmentSeemoreBinding
    private val viewPagerAdapter by lazy { ViewPager2Imgs() }

    private val colorsAdapter by lazy {
        ColorsAdapter { selectedColor ->
            // Handle color click
            Log.d("SeeMoreFragment", "Selected color: $selectedColor")
            // You can do something with the selected color, e.g., update UI or state
        }
    }

    private val sizesAdapter by lazy {
        SizesAdapter { selectedSize ->
            // Handle size click
            Log.d("SeeMoreFragment", "Selected size: $selectedSize")
            // You can do something with the selected size, e.g., update UI or state
        }
    }


    private val vehicleCategories = listOf("Bicycles", "Bikes", "Cars", "Vans", "Buses")
    private val productsCategories = listOf("Epic Products","Offers","Products")
    private val toursCategories = listOf("TTours", "HTours", "", "Food Tours", "Activities")
    private val safetyTipsUrl = "https://www.smartraveller.gov.au/destinations/asia/sri-lanka"
    private val placeUrls = mapOf(
        "Colombo" to "https://www.epicsrilankaholidays.com/sri-lanka-places-to-visit/colombo.html",
        "Galle" to "https://www.epicsrilankaholidays.com/sri-lanka-places-to-visit/south-beach/galle.html"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeemoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        if (product == null) {
            Log.e("SeeMoreFragment", "Product is null")
            return
        }

        setUpSizesRV()
        setUpColorsRV()
        setUpViewPager()

        binding.imgclose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            tvCategoryTitle.text = product.name
            tvCategoryDescription.text = product.description ?: ""
            tvCategoryoldPrice.text = "$ ${String.format("%.2f", product.price ?: 0.0)}"

            if (product.price != null && product.offerPercentage != null) {
                val remainingPricePercentage = 1f - product.offerPercentage
                val priceAfterTheOffer = remainingPricePercentage * product.price
                tvCategorynewPrice.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
                tvCategoryoldPrice.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                tvCategorynewPrice.visibility = View.VISIBLE
                tvCategoryoldPrice.visibility = View.VISIBLE
            } else {
                tvCategorynewPrice.visibility = View.INVISIBLE
            }

            tvCategoryoldPrice.visibility = if (product.price == null) View.INVISIBLE else View.VISIBLE
            tvCategoryDescription.visibility = if (product.description == null) View.INVISIBLE else View.VISIBLE
            tvCategoryColors.visibility = if (product.colors.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            tvCategorySizes.visibility = if (product.sizes.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
        }

        // Convert color integer values to hex strings
        val colorHexStrings = product.colors?.map { colorInt ->
            getColorHexFromInt(colorInt)
        } ?: emptyList()

        colorsAdapter.differ.submitList(colorHexStrings)


        viewPagerAdapter.differ.submitList(product.images)
        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        } ?: Log.e("SeeMoreFragment", "Sizes list is null or empty")

        colorsAdapter.onItemClick = { color ->
            // Save the selected color to a Bundle
            val bundle = Bundle().apply {}

        }

        sizesAdapter.onItemClick = { size ->
            // Save the selected size to a Bundle
            val bundle = Bundle().apply {}
        }

        // Format place name for URL
        val placeName = product.name
        val formattedPlaceName = placeName.replace(" ", "-").toLowerCase()

        // Book Now button
        binding.ButtonbuyNow.setOnClickListener {
            when {
                product.category in vehicleCategories -> {
                    val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToVehicleBookingFragment32(
                        product = product
                    )
                    findNavController().navigate(action)
                }
                placeUrls.containsKey(product.name) -> {
                    val url = placeUrls[product.name]
                    url?.let { openWebPage(it) }
                }
                product.category == "Safety Tips" -> {
                    openWebPage(safetyTipsUrl)
                }
                product.category == "Tour Guides" -> {
                    val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToTourGuideBookingFragment(
                        product = product
                    )
                    findNavController().navigate(action)
                }
                product.category == "TourPacks" -> {
                    val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToTourPackBookingFragment(
                        product = product
                    )
                    findNavController().navigate(action)
                }
                product.category in toursCategories -> {
                    val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToOtherBookingsFragment(
                        product = product
                    )
                    findNavController().navigate(action)
                }
                product.category == "Stays" -> {
                    val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToHotelBookingFragment(
                        product = product
                    )
                    findNavController().navigate(action)
                }
                product.category == "Restaurents" -> {
                    val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToFoodOrderFragment(
                        product = product
                    )
                    findNavController().navigate(action)
                }
                product.category in productsCategories -> {
                    navigateToProductOrderFragment(product)
                }
                else -> {
                    val url = "https://www.epicsrilankaholidays.com/sri-lanka-places-to-visit/$formattedPlaceName.html"
                    openWebPage(url)
                }
            }
        }
    }

    private fun navigateToProductOrderFragment(product: Product) {
        val itemPrice = product.price?.let {
            if (product.offerPercentage != null) {
                val remainingPricePercentage = 1f - product.offerPercentage
                remainingPricePercentage * it
            } else {
                it
            }
        } ?: 0f

        val selectedColor = product.colors?.firstOrNull() ?: "No Color Selected"
        val selectedSize = product.sizes?.firstOrNull() ?: "No Size Selected"

        val action = SeeMoreFragmentDirections.actionSeeMoreFragmentToProductOrderFragment(
            product = product,
            itemName = product.name,
            itemImage = product.images.getOrElse(0) { "" },
            itemPrice = itemPrice,
            itemColor = selectedColor.toString(),
            itemSize = selectedSize
        )
        findNavController().navigate(action)
    }

    private fun getColorHexFromInt(colorInt: Int): String {
        return String.format("#%06X", 0xFFFFFF and colorInt)
    }


    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun setUpViewPager() {
        binding.viewPagerSeeMoreImg.adapter = viewPagerAdapter
    }

    private fun setUpColorsRV() {
        binding.RVcategoryColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpSizesRV() {
        binding.RVcategorySizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
