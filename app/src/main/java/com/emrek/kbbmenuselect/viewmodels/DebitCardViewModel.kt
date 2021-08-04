package com.emrek.kbbmenuselect.viewmodels

import android.transition.Slide
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel
import com.emrek.kbbmenuselect.models.DebitCardModel
import com.emrek.kbbmenuselect.models.FoodModel
import com.emrek.kbbmenuselect.models.OrderModel
import com.emrek.kbbmenuselect.models.ProfileModel

class DebitCardViewModel : ViewModel() {

    private var debitCardModel = MutableLiveData<MutableList<DebitCardModel>>()
    private var debitCardCount = MutableLiveData<Long>()

    fun getDebitCard(): LiveData<MutableList<DebitCardModel>> {
        return debitCardModel
    }

    fun setDebitCard(data: MutableList<DebitCardModel>) {
        debitCardModel.value = data
    }

    fun getDebitCardCount(): LiveData<Long> {
        return debitCardCount
    }

    fun setDebitCardCount(data: Long) {
        debitCardCount.value = data
    }


}