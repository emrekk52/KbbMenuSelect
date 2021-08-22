package com.emrek.kbbmenuselect.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrek.kbbmenuselect.models.FoodModel

class SearchViewModel : ViewModel() {

    private var list = MutableLiveData<MutableList<FoodModel>>()


    fun getQuery(): LiveData<MutableList<FoodModel>> {
        return list
    }

    fun setQuery(data: MutableList<FoodModel>) {
        list.value = data
    }


}