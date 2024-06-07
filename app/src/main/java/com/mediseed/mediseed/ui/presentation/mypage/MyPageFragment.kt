package com.mediseed.mediseed.ui.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediseed.mediseed.databinding.FragmentMypageBinding
import com.mediseed.mediseed.ui.presentation.shared.SharedViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = MyPageAdapter()
        binding.rvMedicineCollection.layoutManager = LinearLayoutManager(context)
        binding.rvMedicineCollection.adapter = adapter

        sharedViewModel.likedItems.observe(viewLifecycleOwner) { likedItems ->
            adapter.submitList(likedItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
