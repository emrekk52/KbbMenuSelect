package com.emrek.kbbmenuselect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.adapters.TabLayoutAdapter
import com.emrek.kbbmenuselect.databinding.ActivityStoryBinding
import com.emrek.kbbmenuselect.databinding.FragmentMainBinding
import com.emrek.kbbmenuselect.tablayouts.*
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI


class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private var adapter: TabLayoutAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        setupTabs()

        return binding!!.root
    }

    private fun setupTabs() {

        adapter = TabLayoutAdapter(activity?.supportFragmentManager!!)
        adapter!!.addFragment(SoupFragment(), "Çorbalar")
        adapter!!.addFragment(MeatFragment(), "Et-Tavuk")
        adapter!!.addFragment(FishFragment(), "Balık")
        adapter!!.addFragment(AppetizerFragment(), "Mezeler")
        adapter!!.addFragment(ColdDrinkFragment(), "Soğuk İçecekler")
        adapter!!.addFragment(HotDrinkFragment(), "Sıcak İçecekler")
        adapter!!.addFragment(CakeFragment(), "Tatlılar")
        adapter!!.addFragment(FruitsFragment(), "Meyveler")
        adapter!!.addFragment(VegetablesFragments(), "Sebzeler")

        binding?.viewPager!!.adapter = adapter
        binding?.tabLayout!!.setupWithViewPager(binding?.viewPager)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding?.viewPager!!.adapter = null
        binding = null
    }

}