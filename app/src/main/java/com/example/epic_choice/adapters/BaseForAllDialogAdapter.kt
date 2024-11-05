package com.example.epic_choice.adapters

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

class BaseForAllDialogAdapter : RecyclerView.Adapter<BaseForAllDialogAdapter.BaseForAllViewHolder>() {

    inner class BaseForAllViewHolder(private val binding: BaseepicdealsRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                // Load image
                if (product.images.isNotEmpty()) {
                    Glide.with(itemView).load(product.images[0]).into(imgEpicDeals)
                } else {
                    imgEpicDeals.setImageDrawable(null) // or set a placeholder image
                }

                // Set price information
                product.offerPercentage?.let { offerPercentage ->
                    val remainingPricePercentage = 1f - offerPercentage
                    val priceAfterTheOffer = remainingPricePercentage * product.price!!
                    tvNewPrice.text = "$ ${String.format("%.2f", priceAfterTheOffer)}"
                    tvOldPrice.text = "$ ${String.format("%.2f", product.price)}"
                    tvOldPrice.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    tvNewPrice.visibility = View.VISIBLE
                    tvOldPrice.visibility = View.VISIBLE
                } ?: run {
                    tvNewPrice.text = "$ ${String.format("%.2f", product.price)}"
                    tvOldPrice.visibility = View.GONE
                    tvNewPrice.visibility = View.VISIBLE
                }

                tvEpicdealProductName.text = product.name

                // Set favorite icon
                val favoriteIconRes = if (product.isFavorited) R.drawable.ic_filledredheart else R.drawable.ic_favorite
                IVfavHeart.setImageResource(favoriteIconRes)

                // Handle favorite click
                IVfavHeart.setOnClickListener {
                    onFavoriteClick?.invoke(product, product.isFavorited) // Trigger favorite click callback
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

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseForAllViewHolder {
        return BaseForAllViewHolder(
            BaseepicdealsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseForAllViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(products: List<Product>) {
        differ.submitList(products)
    }

    fun updateFavoriteStatus(productId: String, isFavorited: Boolean) {
        differ.currentList.find { it.id == productId }?.apply {
            this.isFavorited = isFavorited
            val index = differ.currentList.indexOf(this)
            notifyItemChanged(index)  // Update only the changed item
        }
    }

    var onClick: ((Product) -> Unit)? = null
    var onFavoriteClick: ((Product, Boolean) -> Unit)? = null
}
