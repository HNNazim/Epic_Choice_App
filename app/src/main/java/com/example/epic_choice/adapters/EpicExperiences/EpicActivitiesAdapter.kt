package com.example.epic_choice.adapters.EpicExperiences

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

class EpicActivitiesAdapter: RecyclerView.Adapter<EpicActivitiesAdapter.EpicActivitiesViewHolder>() {

    inner class EpicActivitiesViewHolder(private val binding: BaseepicdealsRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgEpicDeals)

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

                val favoriteIconRes = if (product.isFavorited) R.drawable.ic_filledredheart else R.drawable.ic_favorite
                IVfavHeart.setImageResource(favoriteIconRes)

                IVfavHeart.setOnClickListener {
                    // Toggle favorite status
                    val newFavoriteStatus = !product.isFavorited
                    product.isFavorited = newFavoriteStatus

                    // Trigger favorite click callback
                    onFavoriteClick?.invoke(product, newFavoriteStatus)

                    // Update the icon immediately
                    val updatedIconRes = if (newFavoriteStatus) R.drawable.ic_filledredheart else R.drawable.ic_favorite
                    IVfavHeart.setImageResource(updatedIconRes)

                }            }
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
    ): EpicActivitiesViewHolder {
        return EpicActivitiesViewHolder(
            BaseepicdealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: EpicActivitiesViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun updateFavoriteStatus(productId: String, isFavorited: Boolean) {
        differ.currentList.find { it.id == productId }?.let {
            it.isFavorited = isFavorited
            val position = differ.currentList.indexOf(it)
            notifyItemChanged(position) // Notify adapter of specific item change
        }
    }

    var onFavoriteClick: ((Product, Boolean) -> Unit)? = null
    var onClick:((Product) -> Unit)?= null
}