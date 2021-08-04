package com.emrek.kbbmenuselect.activitys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.adapters.OrderAdapter
import com.emrek.kbbmenuselect.databinding.ActivityOrderBinding
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.OrderModel
import com.emrek.kbbmenuselect.viewmodels.DebitCardViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.menu_design.*
import java.util.*
import kotlin.collections.ArrayList

class OrderActivity : AppCompatActivity() {

    lateinit var binding: ActivityOrderBinding
    var bagsList = mutableListOf<FoodBag>()
    val database = GetFoods().getBags()
    lateinit var orderAdapter: OrderAdapter
    val tax1 = 10.80f
    val tax2 = 0.75f
    var cardCount: Long? = 0

    private lateinit var viewModel: DebitCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderAdapter = OrderAdapter(bagsList, this)
        binding.recyclerView.adapter = orderAdapter

        binding.tax.text = tax1.toString()
        binding.tax2.text = tax2.toString()

        viewModel = ViewModelProvider(this@OrderActivity).get(DebitCardViewModel::class.java)

        getBags()
        setUpButton()

    }

    fun getBags() {


        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bagsList.clear()
                binding.bagTotal.text = "0.0"
                binding.total.text = "0.00"
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
                orderAdapter.notifyDataSetChanged()
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

        binding.buttonOkey.setOnClickListener {

            dialogCreate()
        }

        GetFoods().getCardCount(viewModel)

        viewModel.getDebitCardCount().observe(this, androidx.lifecycle.Observer {
            cardCount = it

        })


    }

    fun dialogCreate() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setMessage("Siparişi onaylamak istiyor musunuz?")
        alertDialogBuilder.setPositiveButton("Evet") { dialog, which ->
            completeOrder()
        }

        alertDialogBuilder.setNegativeButton("Hayır") { dialog, which ->
            dialog.dismiss()
        }


        alertDialogBuilder.show()

    }

    fun completeOrder() {


        if (cardCount!! > 0) {
            val arrayList = ArrayList<FoodBag>()

            for (i in 0..bagsList.size - 1) {

                val fd = FoodBag(
                    bagsList[i].foodName,
                    bagsList[i].foodPicture,
                    bagsList[i].foodPrice,
                    bagsList[i].foodColor,
                    bagsList[i].drinkLiter,
                    bagsList[i].foodID
                )
                fd.bagTotalPrice = bagsList[i].bagTotalPrice
                fd.bagFoodPiece = bagsList[i].bagFoodPiece
                arrayList.add(
                    fd
                )
            }


            val dataModel = OrderModel(
                arrayList,
                binding.total.text.toString(),
                "Visa",
                "Sipariş hazırlanıyor..",
                "",
                tax1.toString(),
                tax2.toString()
            )

            GetFoods().setOrder(dataModel)
            GetFoods().clearBags()


        } else {
            val intent = Intent(this, MyCreditCardActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra("message", "* Öncelikle kart eklemeniz gerekmektedir")
            startActivity(intent)
        }
    }


}