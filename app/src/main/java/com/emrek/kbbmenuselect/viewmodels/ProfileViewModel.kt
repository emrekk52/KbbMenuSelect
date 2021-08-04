package com.emrek.kbbmenuselect.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrek.kbbmenuselect.models.ProfileModel

class ProfileViewModel : ViewModel() {

    private var profile = MutableLiveData<ProfileModel>()
    private var like_count = MutableLiveData<Long>()
    private var order_count = MutableLiveData<Long>()
    private var isAuth = MutableLiveData<Boolean>()

    fun getProfile(): LiveData<ProfileModel> {
        return profile
    }

    fun setProfile(profil: ProfileModel) {
        profile.value = profil
    }


    fun getAuth(): LiveData<Boolean> {
        return isAuth
    }

    fun setAuth(data: Boolean) {
        isAuth.value = data
    }


    fun getLikeCount(): LiveData<Long> {
        return like_count
    }

    fun setLikeCount(count: Long) {
        like_count.value = count
    }

    fun getOrderCount(): LiveData<Long> {
        return order_count
    }

    fun setOrderCount(count: Long) {
        order_count.value = count
    }

}