package com.example.lacocinacompose.models

data class Step(
    val id : String,
    val text: String,
) {
    constructor() : this("","")
}
