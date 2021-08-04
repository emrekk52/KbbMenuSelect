package com.emrek.kbbmenuselect.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emrek.kbbmenuselect.GetFoods
import com.emrek.kbbmenuselect.adapters.CommentAdapter
import com.emrek.kbbmenuselect.databinding.ActivityCommentBinding
import com.emrek.kbbmenuselect.models.CommentModel
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.viewmodels.FoodPreviewModel

class CommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val comment =
            intent.extras?.get("comment") as? ArrayList<CommentModel>


        binding.foodName.text = intent.getStringExtra("food_name")

        val adapter = comment?.let { CommentAdapter(it) }
        binding.commentRecycler.adapter = adapter

        buttonSetup()


    }

    fun buttonSetup() {

        binding.back.setOnClickListener { finish() }

    }


}