package com.mediseed.mediseed.ui.main

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediseed.mediseed.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.mediseed.mediseed.ui.home.SuggestionAdapter
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    private val homeFragment = viewPagerAdapter.getHomeFragment()

    val suggestionRecyclerView: RecyclerView by lazy { binding.suggestionRecyclerview }

    val suggestionAdapter: SuggestionAdapter by lazy {
        SuggestionAdapter(
            onItemClick = { item -> suggestionOnClick(item) }
        )
    }

    private fun suggestionOnClick(item: PharmacyItem.PharmacyInfo) {
        var latitude = item.latitude?.toDoubleOrNull()
        var longitude = item.longitude?.toDoubleOrNull()
        if (latitude != null && longitude != null) {
            homeFragment?.moveCamera(latitude, longitude)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        registerRecyclerView()
        focusControl()
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

        searchBarEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tlMain.visibility = View.INVISIBLE
            } else {
                binding.tlMain.visibility = View.VISIBLE
            }
        }

        // 텍스트 검증
        searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
                clearText()
                val query = text.toString()
                homeFragment?.updateSuggestions(query)
            }

            override fun afterTextChanged(text: Editable?) {
            }
        })

        // 검색 버튼 처리(키보드, 소프트키보드)
        searchBarEditText.setOnEditorActionListener { text, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                hideKeyboard()
                var query = text.text.toString()

                homeFragment?.performSearch(query)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    // Hide TabLayout when Focusing EditText
    private fun focusControl() {
        binding.searchBarEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.tlMain.isVisible = !hasFocus
        }
    }

    // Show TabLayout and Hide Keyboard when OutFocusing of EditText
    @Suppress("UNREACHABLE_CODE")
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val currentView = currentFocus
            if (currentView is EditText) {
                val editTextBox = Rect()
                currentView.getGlobalVisibleRect(editTextBox)
                if (!editTextBox.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    currentView.clearFocus()
                   val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(
                        currentView.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    binding.tlMain.isVisible = true
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun registerRecyclerView() {
        suggestionRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = suggestionAdapter
        }
    }

    private fun clearText() {
        binding.apply {
            clearText.setOnClickListener {
                searchBarEditText.text.clear()
            }
        }
    }

    private fun hideKeyboard() {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun hideBar() {
        binding.apply {
            searchBarEditText.visibility = View.GONE
            searchIcon.visibility = View.GONE
            clearText.visibility = View.GONE
            suggestionRecyclerview.visibility = View.GONE
        }
    }

    fun showBar() {
        binding.apply {
            searchBarEditText.visibility = View.VISIBLE
            searchIcon.visibility = View.VISIBLE
            suggestionRecyclerview.visibility = View.VISIBLE
        }
    }
}



