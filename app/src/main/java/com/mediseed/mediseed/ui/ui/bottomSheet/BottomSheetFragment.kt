package com.mediseed.mediseed.ui.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentBottomSheetBinding
import com.mediseed.mediseed.ui.Const
import com.mediseed.mediseed.ui.ui.home.model.PharmacyItem.PharmacyItem

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var pharmacyInfo: PharmacyItem.PharmacyInfo

    private var turn: Int = 0

    private val viewModel: BottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        arguments?.getParcelable<PharmacyItem.PharmacyInfo>(Const.PHARMACY)?.let { pharmacyInfo ->
                this.pharmacyInfo = pharmacyInfo
            }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            turn = it.getInt("turn", 0)
            binding.tvFacilityType.text = pharmacyInfo.collectionLocationClassificationName
            binding.tvFacilityName.text = pharmacyInfo.collectionLocationName
            binding.tvAddress.text = pharmacyInfo.streetNameAddress
            binding.tvDate.text = pharmacyInfo.dataDate
        }

        // heart count를 관찰
        viewModel.heartCount.observe(viewLifecycleOwner) { count ->
            binding.tvHeartNumber.text = count.toString()
        }

        // heart count를 가져옴
        viewModel.fetchHeartCount(turn)

        // 하트 아이콘 클릭 리스너 설정
        binding.ivHeart.setOnClickListener {
            val isHeartFilled = toggleHeartIcon()
            viewModel.updateHeartCount(turn, isHeartFilled) { success ->
                if (!success) {
                    // 실패했을 경우, 하트 아이콘 변경을 되돌림
                    toggleHeartIcon() // 변경 되돌림
                }
            }
        }
    }

    private fun toggleHeartIcon(): Boolean {
        val isHeartFilled = binding.ivHeart.tag == "filled"
        if (isHeartFilled) {
            binding.ivHeart.setImageResource(R.drawable.ic_heart_empty)
            binding.ivHeart.tag = "empty"
        } else {
            binding.ivHeart.setImageResource(R.drawable.ic_heart_fill)
            binding.ivHeart.tag = "filled"
        }
        return !isHeartFilled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(pharmacyInfo: PharmacyItem.PharmacyInfo): BottomSheetFragment {
            val bundle = Bundle().apply {
                putParcelable(Const.PHARMACY, pharmacyInfo)
            }
            return BottomSheetFragment().apply {
                arguments = bundle
            }

        }
    }

}