package com.mediseed.mediseed.ui.presentation.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()

    }

    private fun initView() = with(binding) {
        // TabLayout x ViewPager2
        vpMain.adapter = viewPagerAdapter
        vpMain.offscreenPageLimit = 1
        vpMain.currentItem = 1 // 초기 페이지를 홈 프래그먼트로 설정
        vpMain.isUserInputEnabled = false // Swipe unabled

        TabLayoutMediator(tlMain, vpMain) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
            tab.setIcon(viewPagerAdapter.getTabIcon(position))
        }.attach()

        tlMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.icon?.setTint(getColor(R.color.green_300))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.icon?.setTint(getColor(R.color.grey))
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 재선택 시
            }
        })

        // 텍스트 검증
        searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = View.VISIBLE
                clearText()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        // 검색 버튼 처리
        searchBarEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // 검색 기능 활성화
                val query =
                    performSearch(textView.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun performSearch(query: String) {
        // 여기서는 간단히 query를 로그에 출력하는 예시를 보여줍니다.
        Log.d("SearchQuery", query)
        // 실제 검색 기능을 활성화하고 결과를 처리하는 코드를 여기에 추가할 수 있습니다.
    }

    private fun clearText() { binding.apply {
        clearText.setOnClickListener {
            searchBarEditText.text.clear()
            clearText.visibility = View.GONE
        }
    }
    }




    fun hideBar() {
        binding.apply {
            searchBarEditText.visibility = View.GONE
            searchIcon.visibility = View.GONE
            clearText.visibility = View.GONE
        }
    }

    fun showBar() {
        binding.apply {
            searchBarEditText.visibility = View.VISIBLE
            searchIcon.visibility = View.VISIBLE
        }
    }
}

