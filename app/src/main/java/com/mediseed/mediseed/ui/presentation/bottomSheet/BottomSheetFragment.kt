package com.mediseed.mediseed.ui.presentation.bottomSheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentBottomSheetBinding
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem
import com.mediseed.mediseed.ui.presentation.shared.SharedViewModel
import com.mediseed.mediseed.ui.share.IntentKey

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var pharmacyInfo: PharmacyItem.PharmacyInfo

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Factory(requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        arguments?.getParcelable<PharmacyItem.PharmacyInfo>(IntentKey.PHARMACY)?.let { pharmacyInfo ->
            this.pharmacyInfo = pharmacyInfo
        }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        with(binding) {
            tvFacilityType.text = pharmacyInfo.CollectionLocationClassificationName
            tvFacilityName.text = pharmacyInfo.CollectionLocationName
            //tvDistance.text = pharmacyInfo.distance
            tvAddress.text = pharmacyInfo.StreetNameAddress
            tvDate.text = pharmacyInfo.DataDate

            if (pharmacyInfo.PhoneNumber.isNullOrEmpty()) {
                llPhone.visibility = View.GONE
            } else {
                tvPhone.text = pharmacyInfo.PhoneNumber
            }

            setupHeartIcon()
            observeViewModel()

            pharmacyInfo.StreetNameAddress?.let {
                sharedViewModel.fetchHeartCount(it)
                sharedViewModel.fetchMedicineCount(it)
            }
        }
    }

    private fun setupHeartIcon() {
        with(binding) {
            ivHeart.setOnClickListener {
                val isHeartFilled = toggleHeartIcon()
                pharmacyInfo.StreetNameAddress?.let { address ->
                    val facilityName = pharmacyInfo.CollectionLocationName ?: ""
                    sharedViewModel.updateHeartCount(address, isHeartFilled, facilityName) { success ->
                        if (success) {
                            if (isHeartFilled) {
                                sharedViewModel.addLikedItem(pharmacyInfo)
                            } else {
                                sharedViewModel.removeLikedItem(pharmacyInfo)
                            }
                        } else {
                            toggleHeartIcon()
                        }
                    }
                }
            }

            if (sharedViewModel.isPharmacyInfoLiked(pharmacyInfo)) {
                ivHeart.setImageResource(R.drawable.ic_heart_fill)
                ivHeart.tag = "filled"
            } else {
                ivHeart.setImageResource(R.drawable.ic_heart_empty)
                ivHeart.tag = "empty"
            }
        }
    }

    private fun toggleHeartIcon(): Boolean {
        with(binding) {
            val isHeartFilled = ivHeart.tag == "filled"
            if (isHeartFilled) {
                ivHeart.setImageResource(R.drawable.ic_heart_empty)
                ivHeart.tag = "empty"
            } else {
                ivHeart.setImageResource(R.drawable.ic_heart_fill)
                ivHeart.tag = "filled"
            }
            return !isHeartFilled
        }
    }

    private fun observeViewModel() {
        with(binding) {
            sharedViewModel.heartCount.observe(viewLifecycleOwner) { count ->
                tvHeartNumber.text = count.toString()
            }

            sharedViewModel.medicineCount.observe(viewLifecycleOwner) { count ->
                tvMedicineNumber.text = count.toString()
            }
        }
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