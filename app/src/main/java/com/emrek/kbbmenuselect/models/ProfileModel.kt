package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class ProfileModel(
    var nameAndSurname: String?,
    var email: String?,
    var phone: String?,
    var userID: String?
) : Parcelable {


    var profilePhotoUrl: String? = null
        get() {
            return field
        }
        set(value) {
            field = value

        }

    var profilePhotoID: String? = null
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameAndSurname)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(userID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileModel> {
        override fun createFromParcel(parcel: Parcel): ProfileModel {
            return ProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<ProfileModel?> {
            return arrayOfNulls(size)
        }
    }


}