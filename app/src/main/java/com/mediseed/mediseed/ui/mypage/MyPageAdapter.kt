package com.mediseed.mediseed.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.ItemFacilityBinding
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

class MyPageAdapter :
    ListAdapter<PharmacyItem.PharmacyInfo, MyPageAdapter.PharmacyViewHolder>(DiffCallback) {

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
            binding.tvTitle.text = item.collectionLocationName
            binding.tvKind.text = item.collectionLocationClassificationName
            binding.tvAddress.text = item.streetNameAddress
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
