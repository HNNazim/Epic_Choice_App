package com.example.epic_choice.adapters.EpicExperiences

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epic_choice.R
import com.example.epic_choice.data.Product
import com.example.epic_choice.databinding.EpicbaseRvItemBinding

class EpicFesAndEventsAdapter: RecyclerView.Adapter<EpicFesAndEventsAdapter.EpicFesAndEventsViewHolder>() {

    inner class EpicFesAndEventsViewHolder(private val binding: EpicbaseRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgEpicPlacesRVItem)
                tvEpicPlacesName.text = product.name
                //tvEpicPlacesDetail.text = product.price.toString()

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

                }
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
    ): EpicFesAndEventsViewHolder {
        return EpicFesAndEventsViewHolder(
            EpicbaseRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: EpicFesAndEventsViewHolder, position: Int) {
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