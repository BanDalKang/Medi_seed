package com.mediseed.mediseed.ui.presentation.bottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentBottomSheetBinding
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem
import com.mediseed.mediseed.ui.share.IntentKey

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var pharmacyInfo: PharmacyItem.PharmacyInfo

    private lateinit var viewModel: BottomSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        arguments?.getParcelable<PharmacyItem.PharmacyInfo>(IntentKey.PHARMACY)?.let { pharmacyInfo ->
            this.pharmacyInfo = pharmacyInfo
        }

        // ViewModel 초기화
        val pref = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val factory = BottomSheetViewModelFactory(pref)
        viewModel = ViewModelProvider(this, factory).get(BottomSheetViewModel::class.java)

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFacilityType.text = pharmacyInfo.CollectionLocationClassificationName
        binding.tvFacilityName.text = pharmacyInfo.CollectionLocationName
        //binding.tvDistance.text = pharmacyInfo.distance
        binding.tvAddress.text = pharmacyInfo.StreetNameAddress
        binding.tvDate.text = pharmacyInfo.DataDate

        if (pharmacyInfo.PhoneNumber.isNullOrEmpty()) {
            binding.llPhone.visibility = View.GONE
        } else {
            binding.tvPhone.text = pharmacyInfo.PhoneNumber
        }

        viewModel.heartCount.observe(viewLifecycleOwner) { count ->
            binding.tvHeartNumber.text = count.toString()
        }

        viewModel.medicineCount.observe(viewLifecycleOwner) { count ->
            binding.tvMedicineNumber.text = count.toString()
        }

        pharmacyInfo.StreetNameAddress?.let { viewModel.fetchHeartCount(it) }
        pharmacyInfo.StreetNameAddress?.let { viewModel.fetchMedicineCount(it) }

        binding.ivHeart.setOnClickListener {
            val isHeartFilled = toggleHeartIcon()
            pharmacyInfo.StreetNameAddress?.let { address ->
                viewModel.updateHeartCount(address, isHeartFilled) { success ->
                    if (success) {
                        if (isHeartFilled) {
                            viewModel.savePharmacyInfoToPrefs(pharmacyInfo)
                        } else {
                            viewModel.removePharmacyInfoFromPrefs(pharmacyInfo)
                        }
                    } else {
                        toggleHeartIcon()
                    }
                }
            }
        }

        // 이미 좋아요가 눌린 상태인지 확인하고 하트 아이콘을 설정
        if (viewModel.isPharmacyInfoLiked(pharmacyInfo)) {
            binding.ivHeart.setImageResource(R.drawable.ic_heart_fill)
            binding.ivHeart.tag = "filled"
        } else {
            binding.ivHeart.setImageResource(R.drawable.ic_heart_empty)
            binding.ivHeart.tag = "empty"
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
                putParcelable(IntentKey.PHARMACY, pharmacyInfo)
            }
            return BottomSheetFragment().apply {
                arguments = bundle
            }
        }
    }
}