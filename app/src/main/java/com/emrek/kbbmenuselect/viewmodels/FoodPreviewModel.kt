package com.emrek.kbbmenuselect.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrek.kbbmenuselect.models.CommentModel
import com.emrek.kbbmenuselect.models.DebitCardModel
import com.emrek.kbbmenuselect.models.ProfileModel

class FoodPreviewModel : ViewModel() {

    private var commentModel = MutableLiveData<ArrayList<CommentModel>>()
    private var commentCount = MutableLiveData<Long>()

    fun getComments(): LiveData<ArrayList<CommentModel>> {
        return commentModel
    }

    fun setComment(data: ArrayList<CommentModel>) {
        commentModel.value = data
    }

    fun getCommentCount(): LiveData<Long> {
        return commentCount
    }

    fun setCommentCount(data: Long) {
        commentCount.value = data
    }

}