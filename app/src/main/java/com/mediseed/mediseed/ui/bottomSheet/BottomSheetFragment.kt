package com.mediseed.mediseed.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentBottomSheetBinding
import com.mediseed.mediseed.utils.Const
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem
import com.mediseed.mediseed.ui.shared.SharedViewModel

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var pharmacyInfo: PharmacyItem.PharmacyInfo

    private val sharedViewModel: SharedViewModel by activityViewModels()

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

        initView()
    }

    private fun initView() {
        setupPharmacyInfo()
        setupHeartIcon()
        observeViewModel()
        fetchCounts()
    }

    private fun setupPharmacyInfo() {
        with(binding) {
            tvFacilityType.text = pharmacyInfo.collectionLocationClassificationName
            tvFacilityName.text = pharmacyInfo.collectionLocationName
            tvDistance.text = pharmacyInfo.distance?.toInt().toString() + " m"
            tvAddress.text = pharmacyInfo.streetNameAddress
            tvDate.text = pharmacyInfo.dataDate

            if (pharmacyInfo.phoneNumber.isNullOrEmpty()) {
                llPhone.visibility = View.GONE
            } else {
                tvPhone.text = pharmacyInfo.phoneNumber
            }
        }
    }

    private fun setupHeartIcon() {
        updateHeartIcon(sharedViewModel.isPharmacyInfoLiked(pharmacyInfo))

        binding.ivHeart.setOnClickListener {
            val isHeartFilled = toggleHeartIcon()
            pharmacyInfo.streetNameAddress?.let { address ->
                val facilityName = pharmacyInfo.collectionLocationName ?: ""
                sharedViewModel.updateHeartCount(address, isHeartFilled, facilityName)
            }
        }
    }

    private fun updateHeartIcon(isLiked: Boolean) {
        if (isLiked) {
            binding.ivHeart.setImageResource(R.drawable.ic_heart_fill)
            binding.ivHeart.tag = "filled"
        } else {
            binding.ivHeart.setImageResource(R.drawable.ic_heart_empty)
            binding.ivHeart.tag = "empty"
        }
    }

    private fun toggleHeartIcon(): Boolean {
        val isHeartFilled = binding.ivHeart.tag == "filled"
        updateHeartIcon(!isHeartFilled)
        return !isHeartFilled
    }

    private fun observeViewModel() {
        with(binding) {
            sharedViewModel.heartCount.observe(viewLifecycleOwner) { count ->
                tvHeartNumber.text = count.toString()
            }

            sharedViewModel.medicineCount.observe(viewLifecycleOwner) { count ->
                tvMedicineNumber.text = count.toString()
            }

            sharedViewModel.updateHeartResult.observe(viewLifecycleOwner) { success ->
                if (success) {
                    if (binding.ivHeart.tag == "filled") {
                        sharedViewModel.addLikedItem(pharmacyInfo)
                    } else {
                        sharedViewModel.removeLikedItem(pharmacyInfo)
                    }
                } else {
                    toggleHeartIcon()
                }
            }

            sharedViewModel.fetchError.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { isError ->
                    if (isError) {
                        Toast.makeText(requireContext(), getString(R.string.fetch_error), Toast.LENGTH_SHORT).show()
                        sharedViewModel.resetFetchError()
                    }
                }
            }
        }
    }

    private fun fetchCounts() {
        pharmacyInfo.streetNameAddress?.let {
            sharedViewModel.fetchHeartCount(it)
            sharedViewModel.fetchMedicineCount(it)
        }
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