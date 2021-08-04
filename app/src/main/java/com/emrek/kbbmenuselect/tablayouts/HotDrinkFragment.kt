package com.emrek.kbbmenuselect.tablayouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.TabLayoutsCreate
import com.emrek.kbbmenuselect.databinding.FragmentFishBinding
import com.emrek.kbbmenuselect.databinding.FragmentHotDrinkBinding
import com.emrek.kbbmenuselect.models.FoodModel
import kotlin.random.Random


class HotDrinkFragment : Fragment() {


    private lateinit var binding: FragmentHotDrinkBinding
    private val REFERENCE_PATH = "hotDrink"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHotDrinkBinding.inflate(layoutInflater)

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