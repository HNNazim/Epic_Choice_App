package com.example.epic_choice.adapters.HomePage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.epic_choice.data.Product
import com.example.epic_choice.databinding.EpicdealsRvItemBinding

class EpicDealsAdapter(
    private val onSeeMoreClick: (String) -> Unit // Callback for "See More" button click with category
) : RecyclerView.Adapter<EpicDealsAdapter.EpicDealsViewHolder>() {

    inner class EpicDealsViewHolder(private val binding: EpicdealsRvItemBinding):ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgEpicDeals)

                itemView.setOnClickListener {
                    Log.d("EpicDealsAdapter", "See More clicked for product: ${product.name}")
                    Toast.makeText(itemView.context, "Clicked on ${product.name}", Toast.LENGTH_SHORT).show()
                    onSeeMoreClick(product.name)
                }

                // Check if the price is not null
                if (product.price != null) {
                    product.offerPercentage?.let { offerPercentage ->
                        val remainingPricePercentage = 1f - offerPercentage
                        val priceAfterTheOffer = remainingPricePercentage * product.price!!
                        tvNewPrice.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
                        tvOldPrice.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                        tvNewPrice.visibility = View.VISIBLE
                        tvOldPrice.visibility = View.VISIBLE
                    }
                    tvOldPrice.text = "$ ${String.format("%.2f", product.price)}"
                    tvOldPrice.visibility = View.VISIBLE
                } else {
                    // Hide price TextViews if the price is null
                    tvNewPrice.visibility = View.GONE
                    tvOldPrice.visibility = View.GONE
                }

                tvEpicdealProductName.text = product.name
            }
        }



    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id ==newItem.id       }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpicDealsViewHolder {
        return EpicDealsViewHolder(
            EpicdealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )
    }

    override fun onBindViewHolder(holder: EpicDealsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}