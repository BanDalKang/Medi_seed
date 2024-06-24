package com.mediseed.mediseed.ui.storage

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
import com.mediseed.mediseed.databinding.FragmentStorageBinding
import com.mediseed.mediseed.ui.shared.SharedViewModel
import com.mediseed.mediseed.ui.main.MainActivity
import com.mediseed.mediseed.ui.sprout.SproutRepository
import com.mediseed.mediseed.ui.sprout.SproutViewModel
import com.mediseed.mediseed.ui.sprout.SproutViewModelFactory

class StorageFragment : Fragment() {
    companion object {
        fun newInstance() = StorageFragment()
    }

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModel.Factory(
            requireContext().getSharedPreferences(
                "prefs",
                Context.MODE_PRIVATE
            )
        )
    }
    private lateinit var adapter: StorageAdapter

    private lateinit var sproutViewModel: SproutViewModel

    private val mainActivity by lazy {
        activity as? MainActivity
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
        val repository = SproutRepository(requireContext())
        val viewModelFactory = SproutViewModelFactory(repository)
        sproutViewModel = ViewModelProvider(this, viewModelFactory).get(SproutViewModel::class.java)
        sproutViewModel.setInitialValues()
    }

    private fun setupRecyclerView() {
        adapter = StorageAdapter()
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