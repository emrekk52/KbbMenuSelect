package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class FoodBag(
    var foodName: String?,
    var foodPicture: String?,
    var foodPrice: String?,
    var foodColor: String?,
    var drinkLiter: String?,
    var foodID: String?
) : Parcelable {


    var bagTotalPrice: String? = null
        get() {
            return field
        }
        set(value) {
            field = value

        }

    var bagFoodPiece: String? = null
        get() {
            return field
        }
        set(value) {
            field = value

        }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodName)
        parcel.writeString(foodPicture)
        parcel.writeString(foodPrice)
        parcel.writeString(foodColor)
        parcel.writeString(drinkLiter)
        parcel.writeString(foodID)
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