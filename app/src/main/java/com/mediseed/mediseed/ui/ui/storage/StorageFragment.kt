package com.mediseed.mediseed.ui.ui.storage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediseed.mediseed.databinding.FragmentMypageBinding
import com.mediseed.mediseed.ui.ui.shared.SharedViewModel
import com.mediseed.mediseed.ui.ui.sprout.SproutViewModel

class StorageFragment : Fragment() {
    companion object {
        fun newInstance() = StorageFragment()
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
    private val sproutViewModel: SproutViewModel by lazy {
        ViewModelProvider(this).get(SproutViewModel::class.java)
    }

    //private lateinit var sproutViewModel: SproutViewModel

    private lateinit var adapter: StorageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        //새싹 뷰모델 observe
        sproutViewModel.shareClickCount.observe(viewLifecycleOwner) {
            binding.tvDrugCount.text = it.toString()
            binding.tvSharingCount.text = it.toString()
        }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = StorageAdapter()
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
