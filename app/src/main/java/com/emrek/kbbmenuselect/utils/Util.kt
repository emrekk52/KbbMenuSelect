package com.emrek.kbbmenuselect.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emrek.kbbmenuselect.R

fun ImageView.downloadImage(url: String?) {

    Glide.with(context).setDefaultRequestOptions(createCircleProgress(context))
        .load(url).into(this)

}


fun createCircleProgress(context: Context): RequestOptions {

    val options = RequestOptions().placeholder(createPlaceHolder(context)).error(R.drawable.avocado)

    return options
}


fun createPlaceHolder(context: Context): CircularProgressDrawable {


    return CircularProgressDrawable(context).apply {
        strokeWidth = 7f
        centerRadius = 40f
        start()

    }
}
