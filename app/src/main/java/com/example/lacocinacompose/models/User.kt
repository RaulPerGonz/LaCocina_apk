package com.example.lacocinacompose.models

data class User(
    val email: String,
    val displayName: String,
    val image: String
){
    constructor() : this("","","")
}
