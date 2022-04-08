package com.example.chattapp.models


class User {
    var displayName: String = ""
    var photoUrl: String = ""
    var email: String = ""
    var uid: String = ""
    var isOnline: Boolean = false

    constructor()

    constructor(
        displayName: String,
        photoUrl: String,
        email: String,
        uid: String,
        isOnline: Boolean
    ) {
        this.displayName = displayName
        this.photoUrl = photoUrl
        this.email = email
        this.uid = uid
        this.isOnline = isOnline
    }

}
