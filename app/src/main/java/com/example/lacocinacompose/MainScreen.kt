package com.example.lacocinacompose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.BottomNavGraph
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Primary_60
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi




@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavHostController, userName: String, userEmail: State<String?>) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = bottomNavController)}

    ) {
        BottomNavGraph(BottomBar_navController = bottomNavController,navController,userName,userEmail)
    }



}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Add,
        BottomBarScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(backgroundColor =  if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,contentColor = Action_color_10){
        screens.forEach { screen ->

            AddItem(
                screen = screen,
                currentDestination = currentDestination ,
                navController = navController
            )
        }

    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    BottomNavigationItem(
        label = {
            Text(text = screen.title)

        },
        alwaysShowLabel = false,
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,

        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true

            }
        }
    )
}

