package com.emrek.kbbmenuselect.tablayouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.TabLayoutsCreate
import com.emrek.kbbmenuselect.databinding.FragmentSoupBinding
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.TabLayoutViewModel
import kotlin.random.Random


class SoupFragment : Fragment() {


    private lateinit var binding: FragmentSoupBinding
    private val REFERENCE_PATH = "soups"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSoupBinding.inflate(layoutInflater)

        TabLayoutsCreate(
            binding.recyclerView,
            requireActivity(),
            requireActivity(),
            requireActivity(),
            Random.nextInt(2),
            requireActivity().supportFragmentManager,
            binding.loadingAnimation,
            REFERENCE_PATH
        )




        return binding.root
    }


}