package com.example.epic_choice.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.epic_choice.databinding.ViewpagerImgItemBinding

class ViewPager2Imgs : RecyclerView.Adapter<ViewPager2Imgs.ViewPager2ImgsViewHolder>() {

    inner class ViewPager2ImgsViewHolder(private val binding: ViewpagerImgItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imagePath: String) {
            Glide.with(itemView).load(imagePath).into(binding.imgCategoryDetails)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImgsViewHolder {
        return ViewPager2ImgsViewHolder(
            ViewpagerImgItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPager2ImgsViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
