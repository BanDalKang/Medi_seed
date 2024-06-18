package com.mediseed.mediseed.ui.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.ItemFacilityBinding
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

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
            binding.tvLocationName.text = item.collectionLocationName
            binding.tvLocationClassificationName.text = item.collectionLocationClassificationName //장소 종류 : 약국, 보건소 등
            binding.tvStreetNameAddress.text = item.streetNameAddress
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PharmacyItem.PharmacyInfo>() {
            override fun areItemsTheSame(
                oldItem: PharmacyItem.PharmacyInfo,
                newItem: PharmacyItem.PharmacyInfo
            ): Boolean {
                return oldItem.streetNameAddress == newItem.streetNameAddress
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
