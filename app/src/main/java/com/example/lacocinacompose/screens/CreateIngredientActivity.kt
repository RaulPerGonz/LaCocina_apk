package com.example.lacocinacompose.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lacocinacompose.login.CustomOutlinedTextField
import com.example.lacocinacompose.models.Ingredient
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.LaCocinaComposeTheme
import com.example.lacocinacompose.ui.theme.Secondary_30
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.*

class CreateIngredientActivity : AppCompatActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            LaCocinaComposeTheme {
                CreateIngredient()
            }
        }
    }

    @Composable
    fun CreateIngredient() {
        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()

        var name by rememberSaveable { mutableStateOf("") }
        var kcal by rememberSaveable { mutableStateOf("") }
        var carbs by rememberSaveable { mutableStateOf("") }
        var fat by rememberSaveable { mutableStateOf("") }
        var protein by rememberSaveable { mutableStateOf("") }
        var gr by rememberSaveable { mutableStateOf("") }


        var validateName by rememberSaveable { mutableStateOf(true) }
        var validateKcal by rememberSaveable { mutableStateOf(true) }
        var validateCarbs by rememberSaveable { mutableStateOf(true) }
        var validateFat by rememberSaveable { mutableStateOf(true) }
        var validateProtein by rememberSaveable { mutableStateOf(true) }
        var validateGr by rememberSaveable { mutableStateOf(true) }


        val validateError = "Campo Obligatorio"




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
                        .clickable { finish() },
                    tint = Action_color_10,

                    )
                Text(
                    text = "Crear Ingrediente",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            }




            CustomOutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre",
                showError = !validateName,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            CustomOutlinedTextField(
                value = kcal,
                onValueChange = { kcal = it },
                label = "Kcal",
                showError = !validateKcal,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            CustomOutlinedTextField(
                value = carbs,
                onValueChange = { carbs = it },
                label = "Carbohidratos",
                showError = !validateCarbs,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            CustomOutlinedTextField(
                value = fat,
                onValueChange = { fat = it },
                label = "Grasas",
                showError = !validateFat,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )



            CustomOutlinedTextField(
                value = protein,
                onValueChange = { protein = it },
                label = "Proteina",
                showError = !validateProtein,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            CustomOutlinedTextField(
                value = gr,
                onValueChange = { gr = it },
                label = "Cantidad (gr)",
                showError = !validateGr,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            val context = LocalContext.current

            Button(
                onClick = {


                    validateName = name.isNotBlank()
                    validateKcal = kcal.isNotBlank()
                    validateCarbs = carbs.isNotBlank()
                    validateFat = fat.isNotBlank()
                    validateProtein = protein.isNotBlank()
                    validateGr = gr.isNotBlank()

                    try{
                        val kcalFloat = kcal.toFloat()
                        val carbsFloat = carbs.toFloat()
                        val fatFloat = fat.toFloat()
                        val proteinFloat = protein.toFloat()
                        val grFloat = gr.toFloat()



                        if (validateName && validateKcal && validateCarbs && validateFat && validateProtein && validateGr) {

                            val db = Firebase.firestore
                            val id = UUID.randomUUID().toString()
                            val ingredient = Ingredient(id,name,kcalFloat,carbsFloat,fatFloat,proteinFloat,grFloat)
                            db.collection("ingredients").document(id).set(ingredient)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        context,
                                        "Ingrediente subido",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }


                            finish()

                        } else {
                        }
                    }
                    catch(e : Exception ){
                        Toast.makeText(
                            context,
                            "Recuerda introducir los datos decimales con un punto no con una coma ;)",
                            Toast.LENGTH_LONG
                        ).show()
                    }




                },


                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(0.9f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Action_color_10,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Añadir Ingrediente", fontSize = 20.sp)
            }
        }

    }


}
