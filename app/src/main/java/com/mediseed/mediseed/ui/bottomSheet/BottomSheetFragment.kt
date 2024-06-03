package com.mediseed.mediseed.ui.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentBottomSheetBinding

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var turn: Int = 0

    private val viewModel: BottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            turn = it.getInt("turn", 0)
            binding.tvFacilityType.text = it.getString("type","")
            binding.tvFacilityName.text = it.getString("name", "")
            binding.tvAddress.text = it.getString("address", "")
            binding.tvPhone.text = it.getString("phone", "")
            binding.tvDate.text = it.getString("date", "")
        }

        viewModel.heartCount.observe(viewLifecycleOwner) { count ->
            binding.tvHeartNumber.text = count.toString()
        }

        viewModel.medicineCount.observe(viewLifecycleOwner) { count ->
            binding.tvMedicineNumber.text = count.toString()
        }

        viewModel.fetchHeartCount(turn)
        viewModel.fetchMedicineCount(turn)

        binding.ivHeart.setOnClickListener {
            val isHeartFilled = toggleHeartIcon()
            viewModel.updateHeartCount(turn, isHeartFilled) { success ->
                if (!success) {
                    toggleHeartIcon()
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
}