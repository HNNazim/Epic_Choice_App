package com.example.epic_choice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic_choice.databinding.SizeRvItemBinding

class SizesAdapter(var onItemClick: (String) -> Unit) : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

    private var selectedPosition = -1

    inner class SizesViewHolder(private val binding: SizeRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String, position: Int) {
            binding.TVSizes.text = size
            binding.imgShadow.visibility = if (position == selectedPosition) View.VISIBLE else View.INVISIBLE
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        return SizesViewHolder(
            SizeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differ.currentList[position]
        holder.bind(size, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick(size)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
