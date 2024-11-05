package com.example.epic_choice.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic_choice.databinding.ColorRvItemBinding

class ColorsAdapter(var onItemClick: (String) -> Unit) : RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {

    private var selectedPosition = -1

    inner class ColorsViewHolder(private val binding: ColorRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(color: String, position: Int) {
            val colorInt = android.graphics.Color.parseColor(color) // Convert color string to int
            val imageDrawable = ColorDrawable(colorInt)
            binding.imgColor.setImageDrawable(imageDrawable)
            binding.imgShadow.visibility = if (position == selectedPosition) View.VISIBLE else View.INVISIBLE
            binding.imgPicked.visibility = if (position == selectedPosition) View.VISIBLE else View.INVISIBLE
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        return ColorsViewHolder(
            ColorRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick(color)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
