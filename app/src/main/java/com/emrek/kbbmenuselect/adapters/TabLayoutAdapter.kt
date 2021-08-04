package com.emrek.kbbmenuselect.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabLayoutAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()


    override fun getCount(): Int {

        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return fragmentTitleList[position]
    }


    fun addFragment(fragment: Fragment, title: String) {

        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }



}