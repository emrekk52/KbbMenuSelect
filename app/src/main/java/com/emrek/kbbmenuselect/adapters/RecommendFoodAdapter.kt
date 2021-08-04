package com.emrek.kbbmenuselect.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.models.FoodModel
import com.squareup.picasso.Picasso

class RecommendFoodAdapter(private var list: List<FoodModel>) :
    RecyclerView.Adapter<RecommendFoodAdapter.ViewHolder>() {

    private var clickListener: RecommendFoodAdapter.ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val foodPicture: ImageView? = view.findViewById(R.id.food_picture)
        val like_count: TextView? = view.findViewById(R.id.like_text)
        val info_text: TextView? = view.findViewById(R.id.info_text)


        fun setUp(data: FoodModel) {

            Picasso.get().load(data.foodPicture.toString()).into(foodPicture)
            like_count?.text = data.food_likes.toString()
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

    fun setOnItemClickListener(clickListener: RecommendFoodAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_food_design, parent, false)
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