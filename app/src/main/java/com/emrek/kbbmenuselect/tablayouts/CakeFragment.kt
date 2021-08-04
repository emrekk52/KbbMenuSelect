package com.emrek.kbbmenuselect.tablayouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.TabLayoutsCreate
import com.emrek.kbbmenuselect.databinding.FragmentAppetizerBinding
import com.emrek.kbbmenuselect.databinding.FragmentCakeBinding
import com.emrek.kbbmenuselect.models.FoodModel
import kotlin.random.Random


class CakeFragment : Fragment() {


    private lateinit var binding: FragmentCakeBinding
    private val REFERENCE_PATH = "cakes"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCakeBinding.inflate(layoutInflater)

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