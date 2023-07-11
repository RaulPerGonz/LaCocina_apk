package com.example.lacocinacompose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector

){
    object Home : BottomBarScreen(
        route = "home",
        title = "Inicio",
        icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        route = "search",
        title = "Buscar",
        icon = Icons.Default.Search
    )

    object Add : BottomBarScreen(
        route = "add",
        title = "Crear",
        icon = Icons.Default.Add
    )

    object Profile : BottomBarScreen(
        route = "profile",
        title = "Perfil",
        icon = Icons.Default.Person
    )

}

