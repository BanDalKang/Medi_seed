package com.mediseed.mediseed.ui.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediseed.mediseed.databinding.FragmentStorageBinding
import com.mediseed.mediseed.ui.shared.SharedViewModel
import com.mediseed.mediseed.ui.main.MainActivity
import com.mediseed.mediseed.ui.sprout.SproutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    private val sproutViewModel: SproutViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var adapter: StorageAdapter

    private val mainActivity by lazy {
        activity as? MainActivity
    }

    companion object {
        fun newInstance() = StorageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewModel()

        sproutViewModel.pillClickCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvMedicineCount.text = count.toString()
        })

        sproutViewModel.shareClickCount.observe(viewLifecycleOwner, Observer { count ->
            binding.tvSharingCount.text = count.toString()
        })

        setupRecyclerView()
    }

    private fun initializeViewModel() {
        sproutViewModel.setInitialValues()
    }

    private fun setupRecyclerView() {
        adapter = StorageAdapter { item ->
            mainActivity?.moveToClickItem(item)
        }
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