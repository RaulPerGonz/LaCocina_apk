package com.example.lacocinacompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lacocinacompose.MainScreen
import com.example.lacocinacompose.Screen
import com.example.lacocinacompose.login.LoginScreen
import com.example.lacocinacompose.login.RegisterScreen
import com.example.lacocinacompose.screens.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    userName: String,
    userEmail: State<String?>

){
    NavHost(navController = navController , startDestination = Screen.Splash.route

    ){
        composable(route = Screen.Main.route){
            MainScreen(navController = navController,userName,userEmail)
        }
        composable(route = Screen.SearchIngredient.route){
            SearchIngredientsActivity()
        }


        composable(route = Screen.Splash.route){
            AnimatedSplashScreen(navController)
        }

        composable(route = Screen.Login.route){
            LoginScreen(Firebase.auth, navController)
        }

        composable(route = Screen.Register.route){
            RegisterScreen(navController = navController)
        }


    }
}
