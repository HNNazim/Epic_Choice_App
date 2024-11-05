package com.example.epic_choice.adapters.EpicStay

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epic_choice.data.Product
import com.example.epic_choice.databinding.Epicbase2RvItemBinding

class EpicStayAdapter(
    private val onSeeMoreClick: (String) -> Unit // Callback for "See More" button click with category
): RecyclerView.Adapter<EpicStayAdapter.EpicSafetyTipsViewHolder>() {

    inner class EpicSafetyTipsViewHolder(private val binding: Epicbase2RvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(imgEpicPlacesRVItem)
                
                tvEpicPlacesName.text = product.name

                itemView.setOnClickListener {
                    Log.d("EpicStayAdapter", "See More clicked for product: ${product.name}")
                    Toast.makeText(itemView.context, "Clicked on ${product.name}", Toast.LENGTH_SHORT).show()
                    onSeeMoreClick(product.name)
                }
                //tvEpicPlacesDetail.text = product.price.toString()
                hearticon.visibility = View.GONE
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
    ): EpicSafetyTipsViewHolder {
        return EpicSafetyTipsViewHolder(
            Epicbase2RvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: EpicSafetyTipsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}