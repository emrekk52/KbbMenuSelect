package com.emrek.kbbmenuselect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.activitys.FoodPreview
import com.emrek.kbbmenuselect.adapters.MyLikeFoodsAdapter
import com.emrek.kbbmenuselect.databinding.FragmentLikeFoodBinding
import com.emrek.kbbmenuselect.models.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class LikeFoodFragment : Fragment() {

    private lateinit var binding: FragmentLikeFoodBinding
    private var likesList = mutableListOf<FoodModel>()
    var likesFood = GetFoods().getLikeFoods()
    var likeAdapter = MyLikeFoodsAdapter(likesList)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLikeFoodBinding.inflate(layoutInflater)

        binding.recycler.adapter = likeAdapter
        binding.recycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        getMyLikeFoods()
        buttonClickSetup()

        return binding.root
    }


    fun buttonClickSetup() {

        likeAdapter.setOnItemClickListener(object : MyLikeFoodsAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {
                if (likesList[position].drinkLiter?.isEmpty() == false && likesList[position].foodPicture?.isEmpty() == false) {
                    bottomSheetDialog(position)
                }
            }
        })


    }

    fun bottomSheetDialog(position: Int) {

        val bundle = Bundle()
        bundle.putParcelable("data", likesList[position])

        val fd = FoodPreview()
        fd.arguments = bundle

        fd.show(activity?.supportFragmentManager!!, "bottomSheetDialog")


    }


    fun getMyLikeFoods() {

        var listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likesList.clear()
                dataSnapshot.children.forEach {
                    var foodModel = FoodModel(
                        it.child("foodName").value.toString(),
                        it.child("foodPicture").value.toString(),
                        it.child("foodDescription").value.toString(),
                        it.child("foodPrice").value.toString(),
                        it.child("foodColor").value.toString(),
                        it.child("drinkLiter").value.toString()
                    )
                    foodModel.foodID = it.child("foodID").value.toString()
                    likesList.add(
                        foodModel
                    )
                }
                if (likesList.size <= 0) {
                    binding.recycler.visibility = View.GONE
                    binding.lottieLike.visibility = View.VISIBLE
                } else {
                    binding.lottieLike.visibility = View.GONE
                    binding.recycler.visibility = View.VISIBLE
                }
                likeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        }

        likesFood.addValueEventListener(listener)

    }

}