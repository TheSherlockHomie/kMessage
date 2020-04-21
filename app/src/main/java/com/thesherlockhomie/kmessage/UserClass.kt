package com.thesherlockhomie.kmessage

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val username: String, val profilePhotoUrl: String) : Parcelable {
    constructor() : this(
        "", "", ""
    )
}