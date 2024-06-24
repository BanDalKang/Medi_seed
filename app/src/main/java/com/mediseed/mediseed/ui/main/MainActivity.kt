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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.mediseed.mediseed.R
import com.mediseed.mediseed.databinding.ActivityMainBinding
import com.mediseed.mediseed.ui.home.SuggestionAdapter
import com.mediseed.mediseed.ui.home.model.pharmacyItem.PharmacyItem
import com.mediseed.mediseed.ui.home.model.viewModel.HomeViewModel
import com.mediseed.mediseed.ui.home.model.viewModel.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy { MainViewPagerAdapter(this) }
    private val homeFragment = viewPagerAdapter.getHomeFragment()
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModelFactory() }
    val suggestionRecyclerView: RecyclerView by lazy { binding.suggestionRecyclerview }
    val suggestionAdapter: SuggestionAdapter by lazy {
        SuggestionAdapter(onItemClick = { item -> suggestionOnClick(item) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setupViewPager()
        setupSearchBar()
        registerRecyclerView()
        focusControl()
    }

    private fun setupViewPager() = with(binding) {
        vpMain.adapter = viewPagerAdapter
        vpMain.offscreenPageLimit = 1
        vpMain.currentItem = 0
        vpMain.isUserInputEnabled = false

        TabLayoutMediator(tlMain, vpMain) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
            tab.setIcon(viewPagerAdapter.getTabIcon(position))
        }.attach()
    }

    private fun setupSearchBar() = with(binding) {
        searchBarEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tlMain.visibility = View.INVISIBLE
            } else {
                binding.tlMain.visibility = View.VISIBLE
            }
        }

        searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrEmpty()) {
                    binding.apply {
                        clearText.visibility = View.GONE
                        searchBarEditText.setBackgroundResource(com.mediseed.mediseed.R.drawable.search_view_background)
                    }
                } else {
                    binding.apply {
                        clearText.visibility = View.VISIBLE
                        searchBarEditText.setBackgroundResource(com.mediseed.mediseed.R.drawable.search_view_changed_background)
                    }
                    clearText()
                    val query = text.toString()
                    homeViewModel.updateSuggestions(query)
                }
            }

            override fun afterTextChanged(text: Editable?) {
            }
        })

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

    private fun registerRecyclerView() {
        suggestionRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = suggestionAdapter
        }
    }

    private fun focusControl() {
        binding.searchBarEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.tlMain.isVisible = !hasFocus
        }
    }

    private fun suggestionOnClick(item: PharmacyItem.PharmacyInfo) {
        var latitude = item.latitude?.toDoubleOrNull()
        var longitude = item.longitude?.toDoubleOrNull()
        if (latitude != null && longitude != null) {
            homeFragment?.moveCamera(latitude, longitude)
        }
        binding.suggestionRecyclerview.visibility = View.INVISIBLE
    }

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

    private fun clearText() {
        binding.apply {
            clearText.setOnClickListener {
                searchBarEditText.text.clear()
                suggestionRecyclerview.visibility = View.INVISIBLE
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

    fun moveToClickItem(item: PharmacyItem.PharmacyInfo) {
        val latitude = item.latitude?.toDoubleOrNull()
        val longitude = item.longitude?.toDoubleOrNull()
        if (latitude != null && longitude != null) {
            binding.vpMain.currentItem = 0
            homeFragment?.moveCamera(latitude, longitude)
        } else {
            Toast.makeText(this, getString(R.string.main_click_error), Toast.LENGTH_SHORT).show()
        }
    }
}