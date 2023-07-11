package com.example.lacocinacompose.models

data class Recipe(
    val id: String,
    val title: String,
    val descript: String,
    val cooking_time: String,
    val favs: Int,
    val image: String

){
    constructor() : this("","","","",0,"")




}

