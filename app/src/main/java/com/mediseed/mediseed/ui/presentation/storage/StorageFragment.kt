package com.mediseed.mediseed.ui.presentation.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mediseed.mediseed.R
import com.mediseed.mediseed.ui.presentation.main.MainActivity

class StorageFragment : Fragment() {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_storage, container, false)
    }

    override fun onResume() {
        super.onResume()
        mainActivity?.hideBar()
    }


}

