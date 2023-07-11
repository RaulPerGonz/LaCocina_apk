package com.example.lacocinacompose

sealed class Screen(val route : String){
    object Main: Screen("main_screen")
    object SearchIngredient : Screen("search_ingredient_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Splash : Screen("splash_screen")
}
