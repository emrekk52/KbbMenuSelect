package com.emrek.kbbmenuselect.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emrek.kbbmenuselect.models.FoodModel

class TabLayoutViewModel : ViewModel() {

    private var soup = MutableLiveData<MutableList<FoodModel>>()
    private var fish = MutableLiveData<MutableList<FoodModel>>()
    private var meat = MutableLiveData<MutableList<FoodModel>>()
    private var appetizer = MutableLiveData<MutableList<FoodModel>>()
    private var cake = MutableLiveData<MutableList<FoodModel>>()
    private var hot_drink = MutableLiveData<MutableList<FoodModel>>()
    private var cold_drink = MutableLiveData<MutableList<FoodModel>>()
    private var vegetables = MutableLiveData<MutableList<FoodModel>>()
    private var fruits = MutableLiveData<MutableList<FoodModel>>()



    fun getFruits(): LiveData<MutableList<FoodModel>> {
        return fruits
    }

    fun setFruits(data: MutableList<FoodModel>) {
        fruits.value = data
    }


    fun getVegetables(): LiveData<MutableList<FoodModel>> {
        return vegetables
    }

    fun setVegetables(data: MutableList<FoodModel>) {
        vegetables.value = data
    }


    fun getColdDrink(): LiveData<MutableList<FoodModel>> {
        return cold_drink
    }

    fun setColdDrink(data: MutableList<FoodModel>) {
        cold_drink.value = data
    }



    fun getHotDrink(): LiveData<MutableList<FoodModel>> {
        return hot_drink
    }

    fun setHotDrink(data: MutableList<FoodModel>) {
        hot_drink.value = data
    }


    fun getSoup(): LiveData<MutableList<FoodModel>> {
        return soup
    }

    fun setSoup(data: MutableList<FoodModel>) {
        soup.value = data
    }


    fun getCake(): LiveData<MutableList<FoodModel>> {
        return cake
    }

    fun setCake(data: MutableList<FoodModel>) {
        cake.value = data
    }


    fun getMeat(): LiveData<MutableList<FoodModel>> {
        return meat
    }

    fun setMeat(data: MutableList<FoodModel>) {
        meat.value = data
    }

    fun getFish(): LiveData<MutableList<FoodModel>> {
        return fish
    }

    fun setFish(data: MutableList<FoodModel>) {
        fish.value = data
    }

    fun getAppetizer(): LiveData<MutableList<FoodModel>> {
        return appetizer
    }

    fun setAppetizer(data: MutableList<FoodModel>) {
        appetizer.value = data
    }

}