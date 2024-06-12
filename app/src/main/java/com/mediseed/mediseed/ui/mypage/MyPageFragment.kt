package com.mediseed.mediseed.ui.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediseed.mediseed.databinding.FragmentMypageBinding
import com.mediseed.mediseed.ui.shared.SharedViewModel
import com.mediseed.mediseed.ui.main.MainActivity
import com.mediseed.mediseed.ui.sprout.SproutViewModel

class MyPageFragment : Fragment() {
    companion object {
        fun newInstance() = MyPageFragment()
    }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Factory(
            requireContext().getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
            )
        )
    }
    private lateinit var adapter: MyPageAdapter

    private val sproutViewModel: SproutViewModel by lazy {
        ViewModelProvider(this).get(SproutViewModel::class.java)
    }

    //바텀 시트 프레그먼트
    private val mainActivity by lazy {
        activity as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        //뷰모델 초기화
        initializeViewModel()

        //전체 약 버리기 갯수
        sproutViewModel.pillClickCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvMedicineCount.text = count.toString()
        })

        //전체 공유 횟수
        sproutViewModel.shareClickCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvSharingCount.text = count.toString()
        })

        setupRecyclerView()

        return binding.root
    }

    private fun initializeViewModel() {
        // ViewModel 초기화를 위해 LiveData 값 설정
        sproutViewModel.setInitialValues()
    }


    private fun setupRecyclerView() {
        adapter = MyPageAdapter()
        binding.rvMedicineList.layoutManager = LinearLayoutManager(context)
        binding.rvMedicineList.adapter = adapter

        sharedViewModel.likedItems.observe(viewLifecycleOwner) { likedItems ->
            adapter.submitList(likedItems)
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity?.hideBar()
        initializeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
