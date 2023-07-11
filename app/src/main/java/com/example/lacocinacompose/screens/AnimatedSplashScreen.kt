package com.example.lacocinacompose.screens


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lacocinacompose.R
import com.example.lacocinacompose.Screen
import com.example.lacocinacompose.login.StoreUser
import com.example.lacocinacompose.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay



@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun AnimatedSplashScreen(navController: NavHostController) {
    var startAnimation by remember{
        mutableStateOf(false)
    }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        )
    )

    val context = LocalContext.current
    val dataStore = StoreUser(context)
    val userEmail = dataStore.getEmail.collectAsState(initial = "")



    LaunchedEffect(key1 = true){
        startAnimation = true
        delay(2000)
        navController.popBackStack()
        if(userEmail.value.isNullOrBlank()){
            navController.navigate(Screen.Login.route)
        }else {
            navController.navigate(Screen.Main.route)
        }

    }



    Splash(alpha = alphaAnim.value)

}

@Composable
fun Splash(alpha: Float) {

    Box(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Icon(
            modifier = Modifier.size(150.dp).alpha(alpha = alpha),
            painter = painterResource(id = R.drawable.la_cocina_logo),
            contentDescription = "splash logo",
            tint = Action_color_10

        )
    }
}






