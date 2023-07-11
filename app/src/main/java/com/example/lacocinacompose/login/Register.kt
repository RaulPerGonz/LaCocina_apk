package com.example.lacocinacompose.login

import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.lacocinacompose.models.User
import com.example.lacocinacompose.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun RegisterScreen(navController: NavHostController) {

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var validateName by rememberSaveable { mutableStateOf(true) }
    var validateSurname by rememberSaveable { mutableStateOf(true) }
    var validateEmail by rememberSaveable { mutableStateOf(true) }
    val validatePhone by rememberSaveable { mutableStateOf(true) }
    var validatePassword by rememberSaveable { mutableStateOf(true) }
    val validateConfirmPassword by rememberSaveable { mutableStateOf(true) }
    var validatePasswordEqual by rememberSaveable { mutableStateOf(true) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val validateNameError = "Porfavor introduzca un nombre valido"
    val validateSurnameError = "Porfavor introduzca un apellido valido"
    val validateEmailError = "Porfavor introduzca un correo valido"
    val validatePhoneError = "Porfavor introduzca un telefono valido"
    val validatePasswordError =
        "La contraseña deberá contener letras mayusculas,minimo un numero y una longitud minima de 8 caracteres"
    val validateEqualPasswordError = "Las contraseñas deben ser iguales"


    fun validateData(
        name: String,
        surname: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        val passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$".toRegex()

        validateName = name.isNotBlank()
        validateSurname = surname.isNotBlank()
        validateEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        validatePassword = passwordRegex.matches(confirmPassword)
        validatePasswordEqual = password == confirmPassword

        return validateName && validateSurname && validateEmail && validatePhone && validatePassword && validateConfirmPassword && validatePasswordEqual
    }


    fun register(
        name: String,
        surname: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (validateData(name, surname, email, password, confirmPassword)) {

            val db = Firebase.firestore
            val user = User(email,name,"")
            db.collection("users").document(email).set(user).addOnSuccessListener {
            }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)




            navController.popBackStack()

        } else {
        }
    }
    Column(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .padding(top = 15.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Button",
                Modifier
                    .padding(end = 90.dp, start = 15.dp)
                    .clickable { navController.popBackStack() },
                tint = Action_color_10,

                )
            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(vertical = 20.dp),
                color = Color.Black
            )
        }


        CustomOutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nombre",
            showError = !validateName,
            errorMessage = validateNameError,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        CustomOutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = "Apellido",
            showError = !validateSurname,
            errorMessage = validateSurnameError,
            leadingIconImageVector = Icons.Default.PermIdentity,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        CustomOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo",
            showError = !validateEmail,
            errorMessage = validateEmailError,
            leadingIconImageVector = Icons.Default.AlternateEmail,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        CustomOutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Telefono",
            showError = !validatePhone,
            errorMessage = validatePhoneError,
            leadingIconImageVector = Icons.Default.Phone,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        CustomOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            showError = !validatePassword,
            errorMessage = validatePasswordError,
            isPasswordField = true,
            isPAsswordVisible = isConfirmPasswordVisible,
            onVisibilityChange = { isConfirmPasswordVisible = it },
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        CustomOutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar contraseña",
            showError = !validateConfirmPassword || !validatePasswordEqual,
            errorMessage = if (!validateConfirmPassword) validatePasswordError else validateEqualPasswordError,
            isPasswordField = true,
            isPAsswordVisible = isConfirmPasswordVisible,
            onVisibilityChange = { isConfirmPasswordVisible = it },
            leadingIconImageVector = Icons.Default.Password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Button(
            onClick = {
                register(name, surname, email, password, confirmPassword)

            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(0.9f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Action_color_10,
                contentColor = Color.White
            )
        ) {
            Text(text = "Registrarse", fontSize = 20.sp)
        }
    }


}


@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    leadingIconImageVector: ImageVector?,
    leadingIconDescription: String = "",
    isPasswordField: Boolean = false,
    isPAsswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String = ""
) {
    val textfieldColors = if (isSystemInDarkTheme())TextFieldDefaults.textFieldColors(
        textColor = Color.White, cursorColor = Shiny_Shamrock, unfocusedIndicatorColor = Color.White, disabledTextColor = Color.White, disabledLabelColor = Color.White, unfocusedLabelColor = Color.White,
    ) else TextFieldDefaults.textFieldColors()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = value, onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 10.dp),
            label = { Text(label) },
            colors = textfieldColors,
            leadingIcon = {
                if (leadingIconImageVector != null) {
                    Icon(
                        imageVector = leadingIconImageVector,
                        contentDescription = leadingIconDescription,
                        tint = if (showError) MaterialTheme.colors.error else (if (isSystemInDarkTheme()) Color.White else Color.Black)
                    )
                }
            },
            isError = showError,
            trailingIcon = {
                if (showError && !isPasswordField) Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Show error"
                )
                if (isPasswordField) {
                    IconButton(onClick = { onVisibilityChange(!isPAsswordVisible) }) {
                        Icon(
                            imageVector = if (isPAsswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                }

            },
            visualTransformation = when {
                isPasswordField && isPAsswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true
        )

        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .offset(y = (-8).dp)
                    .fillMaxWidth(0.9f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegister() {
    RegisterScreen(rememberNavController())
}