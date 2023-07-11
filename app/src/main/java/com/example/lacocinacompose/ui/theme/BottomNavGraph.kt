package com.example.lacocinacompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lacocinacompose.BottomBarScreen
import com.example.lacocinacompose.MainActivity
import com.example.lacocinacompose.Screen

import com.example.lacocinacompose.screens.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

/**
 * Establece la barra de navegación inferior
 *
 * @param BottomBar_navController
 * @param navController
 * @param userName
 * @param userEmail
 */
@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun BottomNavGraph(
    BottomBar_navController: NavHostController,
    navController: NavHostController,
    userName: String,
    userEmail: State<String?>
) {
    NavHost(
        navController = BottomBar_navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(userName,userEmail)
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchScreen()
        }
        composable(route = BottomBarScreen.Add.route) {
            AddScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(route = Screen.Main.route) {
            MainActivity()
        }

    }
}