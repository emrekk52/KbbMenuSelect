package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class StoryModel(
    var brandName: String?,
    var brandPicture: String?,
    var brandContent: String?,
    var contentTime: String?,
    var isTick: Boolean?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brandName)
        parcel.writeString(brandPicture)
        parcel.writeString(brandContent)
        parcel.writeString(contentTime)
        parcel.writeValue(isTick)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoryModel> {
        override fun createFromParcel(parcel: Parcel): StoryModel {
            return StoryModel(parcel)
        }

        override fun newArray(size: Int): Array<StoryModel?> {
            return arrayOfNulls(size)
        }
    }


}