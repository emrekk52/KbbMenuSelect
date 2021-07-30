package com.emrek.kbbmenuselect.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrek.kbbmenuselect.models.ProfileModel

class ProfileViewModel : ViewModel() {

    private var profile = MutableLiveData<ProfileModel>()
    private var like_count = MutableLiveData<Long>()

    fun getProfile(): LiveData<ProfileModel> {
        return profile
    }

    fun setProfile(profil: ProfileModel) {
        profile.value = profil
    }


    fun getLikeCount(): LiveData<Long> {
        return like_count
    }

    fun setLikeCount(count: Long) {
        like_count.value = count
    }

}