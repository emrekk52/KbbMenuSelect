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
import com.emrek.kbbmenuselect.models.CommentModel
import com.emrek.kbbmenuselect.models.FoodModel
import com.squareup.picasso.Picasso
import per.wsj.library.AndRatingBar

class CommentAdapter(private var list: MutableList<CommentModel>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private var clickListener: CommentAdapter.ClickListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val user_picture: ImageView? = view.findViewById(R.id.user_picture)
        val user_email: TextView? = view.findViewById(R.id.user_email)
        val minute: TextView? = view.findViewById(R.id.minute)
        val comment: TextView? = view.findViewById(R.id.comment)
        val ratingBar: AndRatingBar? = view.findViewById(R.id.ratingBar)

        fun setUp(data: CommentModel) {

            Picasso.get().load(data.user_photo.toString()).placeholder(R.drawable.user_login)
                .into(user_picture)
            comment?.text = data.comment
            user_email?.text = data.userEmail
            minute?.text = data.commentTime
            //ratingBar?.isEnabled = false
            ratingBar?.rating = data.ratingCount
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

    fun setOnItemClickListener(clickListener: CommentAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_design, parent, false)
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