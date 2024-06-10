package com.mediseed.mediseed.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.SuggestionRecyclerviewItemBinding
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

class SuggestionAdapter (
    private val onItemClick: (PharmacyItem.PharmacyInfo) -> Unit = {}
) : RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>() {

    private val suggestionItemList = mutableListOf<PharmacyItem.PharmacyInfo>()

    class SuggestionViewHolder(
        var binding: SuggestionRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemPosition: PharmacyItem.PharmacyInfo) = with(binding) {
            pharmacyName.text = itemPosition.collectionLocationName
            pharmacyLocation.text= itemPosition.streetNameAddress
            pharmacyDistance.text = itemPosition.distance?.toInt().toString()
            pharmacyClassification.text = itemPosition.collectionLocationClassificationName.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SuggestionRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
        return SuggestionViewHolder(binding)
    }

    override fun getItemCount(): Int =suggestionItemList.size
    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val itemPosition = suggestionItemList[position]
        holder.apply {
            bind(itemPosition)
            binding.root.setOnClickListener {
                onItemClick(itemPosition)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(suggestionList: List<PharmacyItem.PharmacyInfo>) {
        this.suggestionItemList.clear()
        this.suggestionItemList.addAll(suggestionList)
        notifyDataSetChanged()

    }


}