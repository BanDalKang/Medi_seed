package com.mediseed.mediseed.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        vpMain.offscreenPageLimit = viewPagerAdapter.itemCount
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
    }
}