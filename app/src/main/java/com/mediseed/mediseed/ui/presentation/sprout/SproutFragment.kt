package com.mediseed.mediseed.ui.presentation.sprout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediseed.mediseed.databinding.FragmentHomeBinding

class SproutFragment : Fragment() {

    companion object {
        fun newInstance() = SproutFragment()
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.showBottomSheetButton.setOnClickListener {
//            val bottomSheet = BottomSheetFragment()
//            val bundle = Bundle().apply {
//                putInt("turn", 1)
//                putString("type", "약국")
//                putString("name", "더미 약국")
//                putString("address", "더미구 더미동 123길")
//                putString("phone", "123-456-7890")
//                putString("date", "2024-05-31")
//            }
//            bottomSheet.arguments = bundle
//            bottomSheet.show(childFragmentManager, bottomSheet.tag)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}