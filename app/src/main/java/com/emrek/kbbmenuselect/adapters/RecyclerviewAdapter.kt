package com.emrek.kbbmenuselect.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.activitys.FoodPreview
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.menu_design.view.*

class RecyclerviewAdapter(
    private val dataset: List<FoodModel>,
    private val adsPlace: Int,

    ) :
    RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>() {


    private var clickListener: ClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View
        if (viewType == R.layout.menu_design)
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.menu_design, parent, false)
        else
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.advertise_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerviewAdapter.ViewHolder, position: Int) {


        return holder.setUp(dataset[position], position, adsPlace, dataset.size)

    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemViewType(position: Int): Int {
        if (position == adsPlace || position == dataset.size - 1)
            return R.layout.advertise_design
        else
            return R.layout.menu_design
    }

    override fun getItemCount() = dataset.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {


        val foodPicture: ImageView? = view.findViewById(R.id.foodPicture)
        val foodName: TextView? = view.findViewById(R.id.foodName)
        val foodPrice: TextView? = view.findViewById(R.id.foodPrice)
        val foodDescription: TextView? = view.findViewById(R.id.foodDescription)
        var card: CardView? = view.findViewById(R.id.card)

        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }

        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v, adapterPosition)
            }
        }

        @SuppressLint("ResourceAsColor")
        fun setUp(
            dataset: FoodModel,
            position: Int,
            adsPlace: Int,
            dataSize: Int
        ) {


            if (position != adsPlace && position != dataSize - 1) {
                Picasso.get()
                    .load(dataset?.foodPicture)
                    .placeholder(R.drawable.biggerplaceholder).into(foodPicture)

                if (dataset.drinkLiter == "null")
                    foodName?.text = dataset.foodName
                else
                    foodName?.text = "${dataset.foodName}  ${dataset.drinkLiter}"
                foodDescription?.text = dataset.foodDescription
                foodPrice?.text = "₺" + dataset.foodPrice
                setBackground(dataset.foodColor.toString())
                itemView.addButton.setOnClickListener {
                    val foodBag = FoodBag(
                        dataset.foodName,
                        dataset.foodPicture,
                        dataset.foodPrice,
                        dataset.foodColor,
                        dataset.drinkLiter,
                        dataset.foodID
                    )

                    foodBag.bagTotalPrice = dataset.foodPrice
                    foodBag.bagFoodPiece = "1"

                    GetFoods().addToBag(foodBag)


                }

            }


        }

        @SuppressLint("ResourceAsColor")
        fun setBackground(color: String) {


            when (color) {

                "lightGreen" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#89E18D"))
                }
                "darkGreen" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#4EE455"))
                }

                "lightBlue" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#9FAEFF"))
                }
                "darkBlue" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#4F6AFD"))
                }

                "lightRed" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#FF8484"))
                }
                "darkRed" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#FF4747"))
                }

                "lightYellow" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#FAEC77"))
                }
                "darkYellow" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#DCCB41"))
                }

                "lightOrange" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#FFD08A"))
                }
                "darkOrange" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#FFBF5E"))
                }

                "lightBrown" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#937954"))
                }
                "darkBrown" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#916A31"))
                }

                "lightGray" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#D6D6D6"))
                }
                "darkGray" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#B1B1B1"))
                }

                "lightPurple" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#EEBCF6"))
                }
                "darkPurple" -> {
                    card?.setCardBackgroundColor(Color.parseColor("#C385CD"))
                }


                else -> {
                    card?.setCardBackgroundColor(Color.parseColor("#D6D6D6"))
                }
            }

        }


    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }


}












