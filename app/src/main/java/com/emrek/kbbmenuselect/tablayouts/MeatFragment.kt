package com.emrek.kbbmenuselect.tablayouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.TabLayoutsCreate
import com.emrek.kbbmenuselect.databinding.FragmentHotDrinkBinding
import com.emrek.kbbmenuselect.databinding.FragmentMeatBinding
import com.emrek.kbbmenuselect.models.FoodModel
import kotlin.random.Random


class MeatFragment : Fragment() {


    private lateinit var binding: FragmentMeatBinding
    private val REFERENCE_PATH = "meats"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMeatBinding.inflate(layoutInflater)

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