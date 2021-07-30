package com.emrek.kbbmenuselect.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.adapters.OrderAdapter
import com.emrek.kbbmenuselect.databinding.ActivityOrderBinding
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OrderActivity : AppCompatActivity() {

    lateinit var binding: ActivityOrderBinding
    var bagsList = mutableListOf<FoodBag>()
    val database = GetFoods().getBags()
    lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderAdapter = OrderAdapter(bagsList, this)
        binding.recyclerView.adapter = orderAdapter


        getBags()
        setUpButton()

    }

    fun getBags() {


        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bagsList.clear()
                dataSnapshot.children.forEach {
                    var foodBag = FoodBag(
                        it.child("foodName").value.toString(),
                        it.child("foodPicture").value.toString(),
                        it.child("foodPrice").value.toString(),
                        it.child("foodColor").value.toString(),
                        it.child("drinkLiter").value.toString(),
                        it.child("foodID").value.toString()
                    )

                    foodBag.bagTotalPrice = it.child("bagTotalPrice").value.toString()
                    foodBag.bagFoodPiece = it.child("bagFoodPiece").value.toString()

                    bagsList.add(foodBag)
                }

                if (bagsList.size <= 0) {
                    binding.recyclerView.visibility = View.GONE
                    binding.bottomBar.visibility = View.GONE
                    binding.foodNotFound.visibility = View.VISIBLE
                } else {
                    binding.foodNotFound.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.bottomBar.visibility = View.VISIBLE
                }
                orderAdapter.notifyDataSetChanged ()
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        }




        database.addValueEventListener(listener)

    }

    fun setUpButton() {

        binding
            .back.setOnClickListener {
                finish()
            }


    }

}