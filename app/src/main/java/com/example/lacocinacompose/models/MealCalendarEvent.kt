package com.example.lacocinacompose.models


data class MealCalendarEvent(
    val id: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val meal: String,
    val food: String
){
    constructor() : this ("",0,0,0,"","")
}
