package com.emrek.kbbmenuselect.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrek.kbbmenuselect.models.ProfileModel

class MainViewModel : ViewModel() {


    private var like_count = MutableLiveData<Long>()


    fun getLikeCount(): LiveData<Long> {
        return like_count
    }

    fun setLikeCount(count: Long) {
        like_count.value = count
    }





}