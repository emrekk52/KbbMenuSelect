package com.emrek.kbbmenuselect

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.emrek.kbbmenuselect.activitys.FoodPreview
import com.emrek.kbbmenuselect.adapters.RecyclerviewAdapter
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.TabLayoutViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class TabLayoutsCreate {

    private lateinit var recyclerView: RecyclerView
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var activity: Activity
    private lateinit var viewModelStoreOwner: ViewModelStoreOwner
    private lateinit var list: MutableList<FoodModel>
    private lateinit var supportFragmentManager: FragmentManager
    private lateinit var loadingAnimation: LottieAnimationView
    private var referencePath = ""
    private lateinit var viewModel: TabLayoutViewModel

    constructor(
        recyclerView: RecyclerView,
        activity: Activity,
        viewModelStoreOwner: ViewModelStoreOwner,
        lifecycleOwner: LifecycleOwner,
        adsPlace: Int,
        supportFragmentManager: FragmentManager,
        loadingAnimation: LottieAnimationView,
        referencePath: String
    ) {
        this.recyclerView = recyclerView
        this.activity = activity
        this.viewModelStoreOwner = viewModelStoreOwner
        this.supportFragmentManager = supportFragmentManager
        this.lifecycleOwner = lifecycleOwner
        this.referencePath = referencePath
        this.loadingAnimation = loadingAnimation
        this.viewModel()
    }

    fun getFood() {

        val list = mutableListOf<FoodModel>()
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                list.add(0,FoodModel("", "", "", "", "", "", ""))
                dataSnapshot.children.forEach {
                    var food = FoodModel(
                        it.child("foodName").value.toString(),
                        it.child("foodPicture").value.toString(),
                        it.child("foodDescription").value.toString(),
                        it.child("foodPrice").value.toString(),
                        it.child("foodColor").value.toString(),
                        it.child("drinkLiter").value.toString(),
                        it.child("foodCategory").value.toString()
                    )

                    food.foodID = it.child("foodID").value.toString()
                    food.isOffer = it.child("isOffer").value.toString().toBoolean()

                    if (it.child("food_likes").value.toString() != "null")
                        food.food_likes = it.child("food_likes").value.toString().toInt()

                    food.foodGram = it.child("foodGram").value.toString()

                    food.deliveryTime = it.child("deliveryTime").value.toString()
                    food.foodCalory = it.child("foodCalory").value.toString()


                    list.add(food)


                }

                loadingAnimation.visibility = View.GONE
                list.add( FoodModel("", "", "", "", "", "", ""))

                viewModelSet(list)


            }

            override fun onCancelled(p0: DatabaseError) {

            }

        }

        GetFoods().getFood(referencePath).addValueEventListener(listener)
    }

    fun setRecyclerView(
    ) {
        val rcAdapter = RecyclerviewAdapter(activity, list)
        var staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = rcAdapter


        rcAdapter.setOnItemClickListener(object : RecyclerviewAdapter.ClickListener {
            override fun onItemClick(v: View, position: Int) {

                if (list[position].foodID?.isEmpty() == false && list[position].foodPicture?.isEmpty() == false) {
                    bottomSheetDialog(list[position], supportFragmentManager)
                }
            }

        })

    }

    fun bottomSheetDialog(data: FoodModel, manager: FragmentManager) {

        val bundle = Bundle()
        bundle.putParcelable("data", data)

        val fd = FoodPreview()
        fd.arguments = bundle

        fd.show(manager, "bottomSheetDialog")

    }


    fun viewModelSet(list: MutableList<FoodModel>) {

        when (referencePath) {

            "fruits" -> viewModel.setFruits(list)
            "vegetables" -> viewModel.setVegetables(list)
            "coldDrink" -> viewModel.setColdDrink(list)
            "soups" -> viewModel.setSoup(list)

            "appetizer" -> viewModel.setAppetizer(list)
            "cakes" -> viewModel.setCake(list)
            "fish" -> viewModel.setFish(list)
            "meats" -> viewModel.setMeat(list)
            "hotDrink" -> viewModel.setHotDrink(list)
        }

    }


    fun viewModelGet(): LiveData<MutableList<FoodModel>> {
        lateinit var liveData: LiveData<MutableList<FoodModel>>

        when (referencePath) {
            "fruits" -> liveData = viewModel.getFruits()
            "vegetables" -> liveData = viewModel.getVegetables()
            "coldDrink" -> liveData = viewModel.getColdDrink()
            "soups" -> liveData = viewModel.getSoup()

            "appetizer" -> liveData = viewModel.getAppetizer()
            "cakes" -> liveData = viewModel.getCake()
            "fish" -> liveData = viewModel.getFish()
            "meats" -> liveData = viewModel.getMeat()
            "hotDrink" -> liveData = viewModel.getHotDrink()
        }
        return liveData
    }


    fun viewModel() {

        viewModel = ViewModelProvider(viewModelStoreOwner).get(TabLayoutViewModel::class.java)

        getFood()

        viewModelGet().observe(lifecycleOwner, Observer {
            list = it
            setRecyclerView()
        })

    }


}

private fun Intent.putExtra(s: String, foodModel: FoodModel) {

}