package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class FoodModel(
    var foodName: String?,
    var foodPicture: String?,
    var foodDescription: String?,
    var foodPrice: String?,
    var foodColor: String?,
    var drinkLiter: String?
) : Parcelable {


    var foodID: String? = null
        get() {
            return field
        }
        set(value) {
            field = value

        }

    constructor(parcel: Parcel) : this(
        foodName = parcel.readString(),
        foodPicture = parcel.readString(),
        foodDescription = parcel.readString(),
        foodPrice = parcel.readString(),
        foodColor = parcel.readString(),
        drinkLiter = parcel.readString()
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodName)
        parcel.writeString(foodPicture)
        parcel.writeString(foodDescription)
        parcel.writeString(foodPrice)
        parcel.writeString(foodColor)
        parcel.writeString(drinkLiter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodModel> {
        override fun createFromParcel(parcel: Parcel): FoodModel {
            return FoodModel(parcel)
        }

        override fun newArray(size: Int): Array<FoodModel?> {
            return arrayOfNulls(size)
        }
    }


}