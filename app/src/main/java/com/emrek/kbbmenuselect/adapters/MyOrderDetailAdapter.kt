package com.emrek.kbbmenuselect.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.models.FoodBag
import com.emrek.kbbmenuselect.models.FoodModel
import com.squareup.picasso.Picasso

class MyOrderDetailAdapter(private var list: ArrayList<FoodBag>) :
    RecyclerView.Adapter<MyOrderDetailAdapter.ViewHolder>() {

    private var clickListener: MyOrderDetailAdapter.ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val foodPicture: ImageView? = view.findViewById(R.id.food_picture)
        val food_name: TextView? = view.findViewById(R.id.food_name)
        val food_price: TextView? = view.findViewById(R.id.food_price)
        val total_price: TextView? = view.findViewById(R.id.total_price)
        val count_food: TextView? = view.findViewById(R.id.count_food)


        fun setUp(data: FoodBag) {
            Picasso.get().load(data.foodPicture.toString()).into(foodPicture)
            food_name?.text = data.foodName
            food_price?.text = data.foodPrice
            total_price?.text = data.bagTotalPrice
            count_food?.text = "x"+data.bagFoodPiece
        }

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

    }

    fun setOnItemClickListener(clickListener: MyOrderDetailAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_order_detail_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setUp(list[position])
    }

    override fun getItemCount() = list.size


    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

}