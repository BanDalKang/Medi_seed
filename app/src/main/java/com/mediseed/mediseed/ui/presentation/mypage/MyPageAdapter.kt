package com.mediseed.mediseed.ui.presentation.mypage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.ItemFacilityBinding
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem

class MyPageAdapter : RecyclerView.Adapter<MyPageAdapter.MyPageViewHolder>() {
    interface ItemClick {
        fun onClick(view: View, item: PharmacyItem.PharmacyInfo)
    }

    var itemClick: ItemClick? = null

    //MyPageModel 로 리스트 생성
    private val items: MutableList<PharmacyItem.PharmacyInfo> = mutableListOf()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPageViewHolder {
        val view =
            ItemFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageViewHolder(view)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyPageViewHolder, position: Int) {
        ///holder.bind([position])

        //리스트 아이템 클릭했을 때
        /*holder.itemView.setOnClickListener {
            itemClick?.onClick(it, items[position])
            notifyDataSetChanged() //데이터
        }*/

    }

    override fun getItemCount(): Int = items.size


    //아이템 리스트 업데이트
    @SuppressLint("NotifyDataSetChanged")
    fun updateItem(newItems: List<PharmacyItem.PharmacyInfo>) {
        items.clear()
        items.addAll(newItems.reversed())
        notifyDataSetChanged()
    }

    //MyPage 뷰홀더
    class MyPageViewHolder(private val binding: ItemFacilityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PharmacyItem.PharmacyInfo) = with(binding) {
            tvTitle.text = item.CollectionLocationName
            tvAddress.text = item.StreetNameAddress
        }
    }


}