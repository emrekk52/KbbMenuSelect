package com.emrek.kbbmenuselect.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.activitys.FoodPreview
import com.emrek.kbbmenuselect.adapters.RecyclerviewAdapter
import com.emrek.kbbmenuselect.databinding.FragmentFruitsBinding
import com.emrek.kbbmenuselect.models.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class FruitsFragment : Fragment() {

    private lateinit var binding: FragmentFruitsBinding
    lateinit var rcAdapter: RecyclerviewAdapter
    var list = mutableListOf<FoodModel>()
    val database = GetFoods().getFruits()
    val adsPlace = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFruits()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFruitsBinding.inflate(layoutInflater)

        setRecyclerView()


        return binding.root
    }


    fun setRecyclerView() {
        rcAdapter = RecyclerviewAdapter(list, adsPlace)

        var staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.fruitsRecyclerView.layoutManager = staggeredGridLayoutManager
        binding.fruitsRecyclerView.adapter = rcAdapter

        binding.swipeRefresh.setColorSchemeColors(
            Color.parseColor("#ffdb58"),
            Color.parseColor("#4EE455")
        )

        binding.swipeRefresh.setOnRefreshListener {
            getFruits()
        }

        rcAdapter.setOnItemClickListener(object : RecyclerviewAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {

                if (list[position].drinkLiter?.isEmpty() == false && list[position].foodPicture?.isEmpty() == false) {
                    bottomSheetDialog(position)
                }
            }


        })

    }


    fun bottomSheetDialog(position: Int) {

        val bundle = Bundle()
        bundle.putParcelable("data", list[position])

        val fd = FoodPreview()
        fd.arguments = bundle

        fd.show(activity?.supportFragmentManager!!, "bottomSheetDialog")


    }


    fun getFruits() {
        val postListener = object : ValueEventListener {


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                dataSnapshot.children.forEach {
                    val foodModel = FoodModel(
                        it.child("foodName").value.toString(),
                        it.child("foodPicture").value.toString(),
                        it.child("foodDescription").value.toString(),
                        it.child("foodPrice").value.toString(),
                        it.child("foodColor").value.toString(),
                        it.child("drinkLiter").value.toString()
                    )
                    foodModel.foodID = it.child("foodID").value.toString()
                    list.add(
                        foodModel
                    )
                }
                list.add(adsPlace, FoodModel("", "", "", "", "", ""))
                list.add(FoodModel("", "", "", "", "", ""))

                rcAdapter.notifyDataSetChanged()
                if (binding.swipeRefresh.isRefreshing)
                    binding.swipeRefresh.isRefreshing = false
                binding.loadingAnimation.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("databaseError", "loadPost:onCancelled", databaseError.toException())
            }

        }

        database.addValueEventListener(postListener)


    }


}

private fun <T> Array<T>.set(index: T) {


}

private fun Intent.putExtra(s: String, foodModel: FoodModel) {

}
