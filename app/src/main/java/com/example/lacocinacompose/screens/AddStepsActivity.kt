package com.example.lacocinacompose.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lacocinacompose.R
import com.example.lacocinacompose.ui.theme.*
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

class AddStepsActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            LaCocinaComposeTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
                ) {
                    val listSteps = remember { mutableListOf("") }
                    StepsActivity(listSteps)
                }
            }


        }
    }

    @OptIn(
        com.google.accompanist.permissions.ExperimentalPermissionsApi::class
    )
    @ExperimentalAnimationApi
    @Composable
    private fun StepsActivity(
        ListSteps: MutableList<String>
    ) {

        var text by remember { mutableStateOf("") }



        Scaffold(
            modifier = Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Añadir Pasos") },
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                    navigationIcon = {
                        IconButton(onClick = {


                            setSteps(ListSteps)
                            ListSteps.clear()
                            finish()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "backIcon")
                        }
                    },
                    backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30,
                    contentColor = Color.White,

                    )
            },
        ) {


            var stepsListState by remember { mutableStateOf(getSteps()) }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            ) {

                Column(
                    modifier = Modifier
                        .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val textfieldColors = if (isSystemInDarkTheme())TextFieldDefaults.textFieldColors(
                        textColor = Color.White, cursorColor = Shiny_Shamrock, unfocusedIndicatorColor = Color.White, disabledTextColor = Color.White, disabledLabelColor = Color.White, unfocusedLabelColor = Color.White
                    ) else TextFieldDefaults.textFieldColors()
                    Row {
                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {


                            item {

                                OutlinedTextField(
                                    value = text,
                                    colors = textfieldColors,
                                    onValueChange = { text = it },
                                    singleLine = true,
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            stepsListState =
                                                (stepsListState + listOf(text)) as MutableList<String>
                                        }
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            text = ""

                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Clear,
                                                contentDescription = "Clear"
                                            )
                                        }
                                    },
                                    label = { Text("Añadir Pasos",color = if (isSystemInDarkTheme()) Color.White else Color.Black) }

                                )


                            }


                        }
                    }



                    Box(
                        Modifier
                            .wrapContentSize(Alignment.CenterStart)
                            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60)
                            .animateContentSize(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )


                    ) {


                        StepsList(stepsListState, ListSteps)


                    }


                }


            }
        }


    }


}


@Composable
fun StepsList(stepsState: List<String>, ListSteps: MutableList<String>) {

    val deletedIngredientsList = remember { mutableStateListOf<Int>() }


    ListSteps.clear()
    LazyColumn(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .fillMaxWidth(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(stepsState.size) { index ->


            AnimatedVisibility(

                visible = !deletedIngredientsList.contains(index),
                enter = expandVertically(),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            ) {

                val delete = SwipeAction(
                    icon = painterResource(R.drawable.ic__delete),
                    background = Delete,
                    onSwipe = {
                        deletedIngredientsList.add(index)
                        ListSteps.clear()
                    }
                )


                if (!deletedIngredientsList.contains(index)) {
                    ListSteps.add(stepsState[index])
                }

                SwipeableActionsBox(
                    startActions = listOf(delete),
                    endActions = listOf(delete)
                ) {
                    Row(
                        Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Card(
                            backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RectangleShape,
                            elevation = 0.dp

                        ) {

                            TableCell(text = stepsState[index])


                        }


                    }
                }
            }


        }
    }

}


