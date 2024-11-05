package com.example.epic_choice.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.epic_choice.R
import com.example.epic_choice.data.Address
import com.example.epic_choice.databinding.ItemAddressBinding

class AddressAdapter(private val onClick: (Address) -> Unit) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    private var addresses = listOf<Address>()
    private var selectedPosition: Int = -1

    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: Address?) {
            if (address != null) {
                binding.apply {
                    tvAddressTitle.text = address.addressTitle
                    tvFullName.text = address.fullName
                    tvPhone.text = address.phone
                    tvStreet.text = address.street
                    tvCity.text = address.city
                    tvState.text = address.state

                    root.setOnClickListener {
                        selectedPosition = adapterPosition
                        notifyDataSetChanged()
                        onClick(address)
                    }
                }
            } else {
                binding.apply {
                    tvAddressTitle.text = ""
                    tvFullName.text = ""
                    tvPhone.text = ""
                    tvStreet.text = ""
                    tvCity.text = ""
                    tvState.text = ""

                    root.setOnClickListener(null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    override fun getItemCount() = addresses.size

    fun submitList(newAddresses: List<Address>) {
        addresses = newAddresses
        notifyDataSetChanged()
    }

    fun getSelectedAddress(): Address? {
        return if (selectedPosition != -1) addresses[selectedPosition] else null
    }


}
