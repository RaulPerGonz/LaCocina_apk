package com.example.lacocinacompose.login

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection.Companion.Down
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lacocinacompose.Screen
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Secondary_30
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    navController: NavHostController
) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val isEmailValid by derivedStateOf {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isPasswordValid by derivedStateOf {
        password.length > 7
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    val dataStore = StoreUser(context)








    Column(
        modifier = Modifier
            .background(color = if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "La Cocina",
            style = MaterialTheme.typography.body1,
            fontSize = 50.sp,
            modifier = Modifier.padding(top = 50.dp, bottom = 25.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Correo") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(Down) }
            ), isError = !isEmailValid,
            trailingIcon = {
                if (email.isNotBlank()) {
                    IconButton(onClick = { email = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear email"
                        )
                    }
                }
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Contraseña") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ), isError = !isPasswordValid,
            trailingIcon = {
                if (password.isNotBlank()) {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

        )

        Button(
            onClick = {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            coroutineScope.launch {
                                dataStore.saveEmail(email)
                                dataStore.savePass(password)
                                navController.popBackStack()
                                navController.navigate(Screen.Main.route)
                            }

                        } else {

                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10),
            enabled = isEmailValid && isPasswordValid
        ) {
            Text(
                text = "Entrar",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )
        }



        Button(
            onClick = {
                navController.navigate(Screen.Register.route)
            },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10)
        ) {
            Text(
                text = "Registrarse",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp
            )

        }


    }


}








