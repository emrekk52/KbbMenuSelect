package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class OrderModel(
    var foodBag: ArrayList<FoodBag>,
    var sumTotal: String,
    var paymentMethod: String,
    var orderStatus: String,
    var orderTime: String,
    var tax1: String,
    var tax2: String
) : Parcelable {


    constructor(parcel: Parcel) : this(
        arrayListOf<FoodBag>().apply {
            parcel.readArrayList(FoodBag::class.java.classLoader)
        },
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(foodBag)
        parcel.writeString(sumTotal)
        parcel.writeString(paymentMethod)
        parcel.writeString(orderStatus)
        parcel.writeString(orderTime)
        parcel.writeString(tax1)
        parcel.writeString(tax2)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderModel> {
        override fun createFromParcel(parcel: Parcel): OrderModel {
            return OrderModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderModel?> {
            return arrayOfNulls(size)
        }
    }
}