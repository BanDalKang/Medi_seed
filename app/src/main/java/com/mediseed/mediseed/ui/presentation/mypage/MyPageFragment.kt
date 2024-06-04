package com.mediseed.mediseed.ui.presentation.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private var myAdapter = MyPageAdapter()

    private val viewModel: MyPageViewModel by viewModels<MyPageViewModel>()


    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = ViewModelProvider(requireActivity()).get(MyPageViewModel::class.java)
        //model.likedItems
    }

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//리사이클러뷰 설정
//    private fun setRecyclerView() {
//        binding.rvStorage.adapter = MyPageAdapter {
//        //홈의 약국 타이틀을 가져옴
//        //val bundle = bundleOf("pharmacyId" to it.id)
//
//            //리사이클러뷰 Linearlayout(Horizontal)으로 설정
//            binding.rvStorage.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//
//        }
//    }
}