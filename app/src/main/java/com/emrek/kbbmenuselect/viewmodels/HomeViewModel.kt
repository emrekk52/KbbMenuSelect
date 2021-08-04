package com.emrek.kbbmenuselect.viewmodels

import android.transition.Slide
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.models.ProfileModel

class HomeViewModel : ViewModel() {

    private var best_seller = MutableLiveData<MutableList<FoodModel>>()
    private var shuffle_list = MutableLiveData<MutableList<FoodModel>>()

    private var ads = MutableLiveData<ArrayList<SlideModel>>()


    fun getBestSeller(): LiveData<MutableList<FoodModel>> {
        return best_seller
    }

    fun setBestSeller(data: MutableList<FoodModel>) {
        best_seller.value = data
    }


    fun getAds(): LiveData<ArrayList<SlideModel>> {
        return ads
    }

    fun setAds(data: ArrayList<SlideModel>) {
        ads.value = data
    }


    fun getShuffleList(): LiveData<MutableList<FoodModel>> {
        return shuffle_list
    }

    fun setShuffleList(data: MutableList<FoodModel>) {
        shuffle_list.value = data
    }


}