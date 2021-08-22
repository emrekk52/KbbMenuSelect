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

class SearchAdapter(private var list: List<FoodModel>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var clickListener: SearchAdapter.ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val foodPicture: ImageView? = view.findViewById(R.id.food_picture)
        val food_name: TextView? = view.findViewById(R.id.food_name)
        val food_like: TextView? = view.findViewById(R.id.food_like)
        val food_price: TextView? = view.findViewById(R.id.food_price)


        fun setUp(data: FoodModel) {

            Picasso.get().load(data.foodPicture.toString()).into(foodPicture)
            food_name?.text = data.foodName
            food_like?.text = data.food_likes.toString()
            food_price?.text = data.foodPrice

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

    fun setOnItemClickListener(clickListener: SearchAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_design, parent, false)
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