package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class DebitCardModel(
    var cardNumber: String?,
    var cardCVV: String?,
    var cardSKT: String?,
    var bankName: String?,
    var cardID: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cardNumber)
        parcel.writeString(cardCVV)
        parcel.writeString(cardSKT)
        parcel.writeString(bankName)
        parcel.writeString(cardID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DebitCardModel> {
        override fun createFromParcel(parcel: Parcel): DebitCardModel {
            return DebitCardModel(parcel)
        }

        override fun newArray(size: Int): Array<DebitCardModel?> {
            return arrayOfNulls(size)
        }
    }
}