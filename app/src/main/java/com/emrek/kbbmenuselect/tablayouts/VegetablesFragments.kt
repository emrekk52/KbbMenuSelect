package com.emrek.kbbmenuselect.tablayouts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.TabLayoutsCreate
import com.emrek.kbbmenuselect.activitys.FoodPreview
import com.emrek.kbbmenuselect.adapters.RecyclerviewAdapter
import com.emrek.kbbmenuselect.databinding.FragmentVegetablesBinding
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.TabLayoutViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random


class VegetablesFragments : Fragment() {

    private lateinit var binding: FragmentVegetablesBinding
    private val REFERENCE_PATH = "vegetables"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVegetablesBinding.inflate(layoutInflater)


        TabLayoutsCreate(
            binding.vegetablesRecyclerView,
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

