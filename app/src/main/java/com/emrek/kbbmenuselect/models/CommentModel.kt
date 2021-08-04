package com.emrek.kbbmenuselect.models

import android.os.Parcel
import android.os.Parcelable

class CommentModel(
    var comment: String?,
    var userEmail: String?,
    var commentTime: String?,
    var userUid: String?,
    var food_id: String?,
    var ratingCount: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat()
    ) {
    }

    var user_photo: String? = null
        get() {
            return field
        }
        set(value) {
            field = value
        }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(comment)
        parcel.writeString(userEmail)
        parcel.writeString(commentTime)
        parcel.writeString(userUid)
        parcel.writeString(food_id)
        parcel.writeFloat(ratingCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommentModel> {
        override fun createFromParcel(parcel: Parcel): CommentModel {
            return CommentModel(parcel)
        }

        override fun newArray(size: Int): Array<CommentModel?> {
            return arrayOfNulls(size)
        }
    }
}