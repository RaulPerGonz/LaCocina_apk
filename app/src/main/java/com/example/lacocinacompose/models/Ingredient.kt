package com.example.lacocinacompose.models

data class Ingredient(
    val id: String,
    val name: String,
    val kcal: Float,
    val carbs: Float,
    val fat: Float,
    val protein: Float,
    var gr: Float,
){
    constructor() : this ("","",0.0f,0.0f,0.0f,0.0f,0.0f)

}
