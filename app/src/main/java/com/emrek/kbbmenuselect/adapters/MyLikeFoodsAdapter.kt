package com.emrek.kbbmenuselect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.models.FoodModel
import com.squareup.picasso.Picasso

class MyLikeFoodsAdapter(private var likesList: List<FoodModel>) :
    RecyclerView.Adapter<MyLikeFoodsAdapter.ViewHolder>() {

    private var clickListener: MyLikeFoodsAdapter.ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_likes_design, parent, false)

        return ViewHolder(view)
    }


    fun setOnItemClickListener(clickListener: MyLikeFoodsAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setUp(likesList[position])
    }

    override fun getItemCount() = likesList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {


        val foodPicture: ImageView? = view.findViewById(R.id.food_picture)
        val likeButton: ImageView? = view.findViewById(R.id.like_button)
        val foodName: TextView? = view.findViewById(R.id.food_name)
        val foodDescription: TextView? = view.findViewById(R.id.food_description)


        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }

        fun setUp(data: FoodModel) {
            Picasso.get().load(data.foodPicture).placeholder(R.drawable.biggerplaceholder)
                .into(foodPicture)

            foodName?.text = data.foodName
            foodDescription?.text = data.foodDescription
            likeButton?.setOnClickListener {
                GetFoods().getLikeFoods().child(data.foodID.toString()).removeValue()
                if (data.food_likes.toString() != "null")
                    data.food_likes = data.food_likes!!.toInt() - 1
                GetFoods().updateFood(data)
            }
        }


        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v, adapterPosition)
            }
        }


    }


    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

}