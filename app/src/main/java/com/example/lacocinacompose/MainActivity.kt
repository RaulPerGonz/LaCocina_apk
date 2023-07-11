package com.example.lacocinacompose

import android.content.ContentValues
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.lacocinacompose.login.StoreUser
import com.example.lacocinacompose.models.User
import com.example.lacocinacompose.ui.theme.LaCocinaComposeTheme
import com.example.lacocinacompose.ui.theme.SetupNavGraph
import com.example.lacocinacompose.utils.LockScreenOrientation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {


    private val auth by lazy {
        Firebase.auth
    }






    @ExperimentalPermissionsApi
    @ExperimentalFoundationApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LaCocinaComposeTheme {



                LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                actionBar?.hide()

                val navController = rememberNavController()

                val context = LocalContext.current
                val dataStore = StoreUser(context)
                val userEmail = dataStore.getEmail.collectAsState(initial = "")
                val db = Firebase.firestore
                var userName by remember { mutableStateOf("") }



                db.collection("users").whereEqualTo("email", userEmail.value).get()
                    .addOnSuccessListener { users ->
                        for (user in users) {
                            Log.i("users", "${user.id} => ${user.data}")
                            val user = user.toObject(User::class.java)
                            userName = user.displayName
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(ContentValues.TAG, "Error getting documents: ", exception)
                    }

                SetupNavGraph(navController = navController ,userName,userEmail)

                /*
                CompositionLocalProvider(UserState provides userState) {
                    ApplicationSwitcher()
                }

                 */






            }
        }
    }

}



