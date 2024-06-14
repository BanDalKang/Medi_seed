package com.mediseed.mediseed.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mediseed.mediseed.R
import com.mediseed.mediseed.ui.storage.StorageFragment
import com.mediseed.mediseed.ui.sprout.SproutFragment
import com.mediseed.mediseed.ui.home.HomeFragment

class MainViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        MainTabModel(
            SproutFragment.newInstance(),
            R.string.main_tab_sprout_title,
            R.drawable.ic_sprout
        ),
        MainTabModel(HomeFragment.newInstance(), R.string.main_tab_home_title, R.drawable.ic_home),
        MainTabModel(
            StorageFragment.newInstance(),
            R.string.main_tab_mypage_title,
            R.drawable.ic_mypage
        ),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position].fragment


    fun getTitle(position: Int): Int = fragments[position].title

    fun getTabIcon(position: Int): Int = fragments[position].icon

    fun getHomeFragment(): HomeFragment? = fragments.getOrNull(1)?.fragment as? HomeFragment

}
