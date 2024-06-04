package com.mediseed.mediseed.ui.presentation.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.ItemFacilityBinding

class MyPageAdapter : RecyclerView.Adapter<MyPageAdapter.MyPageViewHolder>() {
    interface ItemClick {
        fun onClick(view: View, item: MyPageViewModel)
    }

    var itemClick: ItemClick? = null
    private val items: MutableList<MyPageViewModel> = mutableListOf()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPageViewHolder {
        val view =
            ItemFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        //holder.bind(items[position])
        holder.itemView.setOnClickListener {

        }

    }

    override fun getItemCount(): Int = items.size


    //아이템 리스트 업데이트
    fun updateItem() {

    }

    //뷰홀더
    class MyPageViewHolder(private val binding: ItemFacilityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //바인딩 데이터 맞추기 리사이클러뷰의 아이템 = 모델의 아이템 ?
        fun bind(item: MyPharmacyModel) = with(binding) {
            tvTitle.text = item.title
            tvAddress.text = item.address
        }
    }


}