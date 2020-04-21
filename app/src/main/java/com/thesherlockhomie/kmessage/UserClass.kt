package com.thesherlockhomie.kmessage

class User(val uid: String, val username: String, val profilePhotoUrl: String) {
    constructor() : this(
        "", "", ""
    )
}