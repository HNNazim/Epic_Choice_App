package com.example.epic_choice.adapters.EpicBuy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epic_choice.R
import com.example.epic_choice.data.Product
import com.example.epic_choice.databinding.BaseepicdealsRvItemBinding

class EpicProductOffersAdapter : RecyclerView.Adapter<EpicProductOffersAdapter.EpicProductOffersViewHolder>() {

    inner class EpicProductOffersViewHolder(private val binding: BaseepicdealsRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                // Load product image
                Glide.with(itemView).load(product.images.getOrNull(0)).into(imgEpicDeals)

                // Handle pricing
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
                    tvNewPrice.visibility = View.GONE
                    tvOldPrice.visibility = View.GONE
                }

                // Set product name
                tvEpicdealProductName.text = product.name

                // Set favorite icon
                val favoriteIconRes = if (product.isFavorited) R.drawable.ic_filledredheart else R.drawable.ic_favorite
                IVfavHeart.setImageResource(favoriteIconRes)

                // Handle favorite icon click
                IVfavHeart.setOnClickListener {
                    // Toggle favorite status
                    val newFavoriteStatus = !product.isFavorited
                    product.isFavorited = newFavoriteStatus

                    // Trigger favorite click callback
                    onFavoriteClick?.invoke(product, newFavoriteStatus)

                    // Update the icon immediately
                    val updatedIconRes = if (newFavoriteStatus) R.drawable.ic_filledredheart else R.drawable.ic_favorite
                    IVfavHeart.setImageResource(updatedIconRes)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpicProductOffersViewHolder {
        return EpicProductOffersViewHolder(
            BaseepicdealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EpicProductOffersViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        // Handle item click
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun updateProducts(products: List<Product>?) {
        differ.submitList(products ?: emptyList()) // Handle null case
    }

    fun updateFavoriteStatus(productId: String, isFavorited: Boolean) {
        differ.currentList.find { it.id == productId }?.let {
            it.isFavorited = isFavorited
            val position = differ.currentList.indexOf(it)
            notifyItemChanged(position) // Notify adapter of specific item change
        }
    }

    var onClick: ((Product) -> Unit)? = null
    var onFavoriteClick: ((Product, Boolean) -> Unit)? = null
}
