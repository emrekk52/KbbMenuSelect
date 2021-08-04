package com.emrek.kbbmenuselect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.activitys.FoodPreview
import com.emrek.kbbmenuselect.adapters.BestSellerAdapter
import com.emrek.kbbmenuselect.adapters.RecommendFoodAdapter
import com.emrek.kbbmenuselect.databinding.FragmentHomeBinding
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.HomeViewModel
import java.util.ArrayList


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        getBestSeller()
        getShuffleList()
        getAds()


        return binding.root
    }


    fun getShuffleList() {

        GetFoods().getShuffleFoodList(viewModel)

        viewModel.getShuffleList().observe(requireActivity(), Observer {

            val recommendAdapter = RecommendFoodAdapter(it)
            binding.recommendRecycler.adapter = recommendAdapter

            recommendAdapter.setOnItemClickListener(object : RecommendFoodAdapter.ClickListener {
                override fun onItemClick(v: View, position: Int) {
                    if (it[position].foodID?.isEmpty() == false && it[position].foodPicture?.isEmpty() == false) {
                        getBottomSheet(it[position])
                    }
                }
            })

        })

    }


    fun getBestSeller() {

        GetFoods().getBestSeller(viewModel)

        viewModel.getBestSeller().observe(requireActivity(), Observer {
            val bstAdapter = BestSellerAdapter(it)
            binding.bestRecycler.adapter = bstAdapter


            bstAdapter.setOnItemClickListener(object : BestSellerAdapter.ClickListener {
                override fun onItemClick(v: View, position: Int) {
                    if (it[position].foodID?.isEmpty() == false && it[position].foodPicture?.isEmpty() == false) {
                        getBottomSheet(it[position])
                    }

                }
            })
        })


    }

    fun getBottomSheet(data: FoodModel) {

        val bundle = Bundle()
        bundle.putParcelable("data", data)

        val fd = FoodPreview()
        fd.arguments = bundle

        fd.show(activity?.supportFragmentManager!!, "bottomSheetDialog")

    }

    fun getAds() {

        GetFoods().getAds(viewModel)

        viewModel.getAds().observe(requireActivity(), Observer {
            binding.adsSlider.setImageList(it, ScaleTypes.CENTER_CROP)

        })

    }

}