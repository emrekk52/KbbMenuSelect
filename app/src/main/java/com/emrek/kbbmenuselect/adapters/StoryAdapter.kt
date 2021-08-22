package com.emrek.kbbmenuselect.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emrek.kbbmenuselect.R
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.models.StoryModel
import com.emrek.kbbmenuselect.utils.downloadImage
import com.squareup.picasso.Picasso
import kotlin.coroutines.coroutineContext

class StoryAdapter(private var storyList: List<StoryModel>, var context: Context) :
    RecyclerView.Adapter<StoryAdapter.ViewHolder>() {


    private var clickListener: StoryAdapter.ClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryAdapter.ViewHolder, position: Int) {
        holder.setUp(storyList[position])
    }

    fun setOnItemClickListener(clickListener: StoryAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun getItemCount() = storyList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val food_picture: ImageView? = view.findViewById(R.id.food_picture)
        val foodName: TextView? = view.findViewById(R.id.food_name)

        fun setUp(data: StoryModel) {

            /*  Picasso.get().load(data.brandPicture).placeholder(R.drawable.littleplaceholder)
                  .centerCrop().into(food_picture)
  */

            food_picture?.downloadImage(data.brandPicture)


            foodName?.text = data.brandName
            foodName?.isSelected = true
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

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }


}