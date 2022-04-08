package com.example.chattapp.models

class Message {

    var from: String = ""
    var to: String = ""
    var message: String = ""

    constructor(from: String, to: String, message: String) {
        this.from = from
        this.to = to
        this.message = message
    }

    constructor()

}
