package com.mediseed.mediseed.ui.ui.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.ItemFacilityBinding
import com.mediseed.mediseed.ui.ui.home.model.PharmacyItem

class StorageAdapter :
    ListAdapter<PharmacyItem.PharmacyInfo, StorageAdapter.PharmacyViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
        val binding =
            ItemFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PharmacyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PharmacyViewHolder(private val binding: ItemFacilityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PharmacyItem.PharmacyInfo) {
            binding.tvTitle.text = item.CollectionLocationName
            binding.tvKind.text = item.CollectionLocationClassificationName
            binding.tvAddress.text = item.StreetNameAddress
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PharmacyItem.PharmacyInfo>() {
            override fun areItemsTheSame(
                oldItem: PharmacyItem.PharmacyInfo,
                newItem: PharmacyItem.PharmacyInfo
            ): Boolean {
                return oldItem.StreetNameAddress == newItem.StreetNameAddress
            }

            override fun areContentsTheSame(
                oldItem: PharmacyItem.PharmacyInfo,
                newItem: PharmacyItem.PharmacyInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
