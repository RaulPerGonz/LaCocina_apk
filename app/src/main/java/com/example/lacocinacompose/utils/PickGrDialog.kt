package com.example.lacocinacompose.utils

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lacocinacompose.models.Ingredient
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Primary_60

@Composable
fun PickGrDialog(setShowDialog: (Boolean) -> Unit, list: MutableList<Ingredient>, index: Int) {



    Dialog(onDismissRequest = { setShowDialog(false) }) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(10.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Modificar Cantidad",

                            )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "",
                            tint = colorResource(R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))




                    var gr by remember { mutableStateOf(("")) }
                    TextField(
                        value = gr,
                        label = { Text(text = "Cantidad (gr)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            gr = it
                        }


                    )


                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if(gr.isNotEmpty()) {
                                    list[index].gr = gr.toFloat()
                                }
                                setShowDialog(false)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "OK")
                        }
                    }


                }
            }
        }

    }


}

