package com.example.typeapp.Models

class Message {
    var message :String? = null
    var senderid : String? = null

    constructor()

    constructor(message: String?,senderid : String? ){
        this.message = message
        this.senderid = senderid
    }
}