package com.emrek.kbbmenuselect.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.models.FoodModel
import com.squareup.picasso.Picasso

class BestSellerAdapter(private var list: List<FoodModel>) :
    RecyclerView.Adapter<BestSellerAdapter.ViewHolder>() {

    private var clickListener: BestSellerAdapter.ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val foodPicture: ImageView? = view.findViewById(R.id.food_picture)
        val star_count: TextView? = view.findViewById(R.id.star_count)
        val offer_layout: RelativeLayout? = view.findViewById(R.id.offer_layout)

        fun setUp(data: FoodModel) {

            Picasso.get().load(data.foodPicture.toString()).into(foodPicture)
            if (data.isOffer == true)
                offer_layout?.visibility = View.VISIBLE
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

    fun setOnItemClickListener(clickListener: BestSellerAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.best_seller, parent, false)
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