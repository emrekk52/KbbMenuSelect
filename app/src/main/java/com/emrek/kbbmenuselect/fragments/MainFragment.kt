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
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        setupTabs()



        return binding.root
    }

    private fun setupTabs() {

        val adapter = TabLayoutAdapter(activity?.supportFragmentManager!!)
        adapter.addFragment(FruitsFragment(), "Meyveler")
        adapter.addFragment(VegetablesFragments(), "Sebzeler")
        adapter.addFragment(ColdDrinkFragment(), "Soğuk İçecekler")
        adapter.addFragment(VegetablesFragments(), "Sıcak İçecekler")
        adapter.addFragment(VegetablesFragments(), "Atıştırmalıklar")
        adapter.addFragment(VegetablesFragments(), "Atıştırmalıklar")
        adapter.addFragment(VegetablesFragments(), "Atıştırmalıklar")
        adapter.addFragment(VegetablesFragments(), "Atıştırmalıklar")

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

}