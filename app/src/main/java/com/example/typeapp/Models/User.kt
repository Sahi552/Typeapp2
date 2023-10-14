package com.example.typeapp.Models

class User{
    var name:String? = null
    var imageurl:String? = null
    var uid:String? = null

    constructor(){}

    constructor(name: String?,imageurl: String?,uid : String?){
        this.name = name
        this.imageurl = imageurl
        this.uid = uid
    }

}