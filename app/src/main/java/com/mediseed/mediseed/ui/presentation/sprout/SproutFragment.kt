package com.mediseed.mediseed.ui.presentation.sprout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediseed.mediseed.databinding.FragmentSproutBinding
import com.mediseed.mediseed.ui.presentation.main.MainActivity

class SproutFragment : Fragment() {

    companion object {
        fun newInstance() = SproutFragment()
    }
    private var _binding: FragmentSproutBinding? = null
    private val binding get() = _binding!!

    private val mainActivity by lazy {
        activity as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSproutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainActivity?.hideBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}