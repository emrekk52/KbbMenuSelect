package com.emrek.kbbmenuselect.viewmodels

import android.transition.Slide
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.models.OrderModel
import com.emrek.kbbmenuselect.models.ProfileModel

class OrderViewModel : ViewModel() {

    private var orderModel = MutableLiveData<MutableList<OrderModel>>()


    fun getOrders(): LiveData<MutableList<OrderModel>> {
        return orderModel
    }

    fun setOrders(data: MutableList<OrderModel>) {
        orderModel.value = data
    }


}