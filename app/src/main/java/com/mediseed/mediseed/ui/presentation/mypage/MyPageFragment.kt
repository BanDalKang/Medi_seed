package com.mediseed.mediseed.ui.presentation.mypage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediseed.mediseed.databinding.FragmentMypageBinding
import com.mediseed.mediseed.ui.presentation.bottomSheet.BottomSheetViewModelFactory
import com.mediseed.mediseed.ui.presentation.home.model.PharmacyItem
import kotlinx.coroutines.launch

class MyPageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private var myAdapter = MyPageAdapter()

    private val viewModel: MyPageViewModel by viewModels<MyPageViewModel>()


    companion object {
        const val BUNDLE_KEY_FACILITY = "BUNDLE_KEY_MY_PAGE_FRAGMENT"
        fun newInstance() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        setRecyclerView()
        observeRecyclerViewItem()

        //sharedpreferences 받아오기
        val pref = requireContext().getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
        val factory = BottomSheetViewModelFactory(pref)
        viewModel = ViewModelProvider(this, factory).get(MyPageViewModel::class.java)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    //LinearLayout으로 리사이클러뷰 설정
    private fun setRecyclerView() {
        binding.rvMedicineCollection.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvMedicineCollection.adapter = myAdapter

    }

    //내가 처리한 약 갯수
    private fun medeicneCount() {

    }

    //내가 앱을 공유한 횟수
    private fun sharingCount() {

    }


    //아이템이 클릭되지 않은 상태
    private var isItemClickEnabled = true

    private fun clickItem(item: PharmacyItem.PharmacyInfo) {
        if (!isItemClickEnabled) return
        isItemClickEnabled = false


        val bundle = Bundle().apply {
            putParcelable(BUNDLE_KEY_FACILITY, item)
        }

        //val bottomFragment = BottomSheetFragment.newInstance(pharmacyInfo = PharmacyItem.PharmacyInfo(item))
        //(requireActivity() as MainActivity).
    }


    //리사이클러뷰 설정
    private fun setList() {
        setFragmentResultListener("item_list") { requestKey, bundle ->
            val isLiskedChanged = bundle.getBoolean("is_liked_changed")

            //true
            if (isLiskedChanged) {
                viewModel.getLikedItems()
            }
        }
    }


    private fun observeRecyclerViewItem() = lifecycleScope.launch {
        viewModel.likedItems.observe(viewLifecycleOwner) {
            val adapter = (binding.rvMedicineCollection.adapter as? MyPageAdapter) ?: return@observe
            adapter.updateItem(it)
        }
    }


    override fun onResume() {
        super.onResume()
        //스크롤 위치 초기화 추가 할지 말지
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
