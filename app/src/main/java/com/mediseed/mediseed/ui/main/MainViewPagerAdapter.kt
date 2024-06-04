package com.mediseed.mediseed.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mediseed.mediseed.R
import com.mediseed.mediseed.ui.mypage.MyPageFragment
import com.mediseed.mediseed.ui.home.HomeFragment
import com.mediseed.mediseed.ui.sprout.SproutFragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        MainTabModel(SproutFragment.newInstance(), R.string.main_tab_sprout_title, R.drawable.ic_sprout),
        MainTabModel(HomeFragment.newInstance(), R.string.main_tab_home_title, R.drawable.ic_home),
        MainTabModel(MyPageFragment.newInstance(), R.string.main_tab_mypage_title, R.drawable.ic_mypage),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].fragment

    fun getTitle(position: Int): Int = fragments[position].title

    fun getTabIcon(position: Int): Int = fragments[position].icon
}