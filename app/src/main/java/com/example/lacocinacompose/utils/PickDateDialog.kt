package com.example.lacocinacompose.utils

import android.R
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lacocinacompose.login.StoreUser
import com.example.lacocinacompose.models.MealCalendarEvent
import com.example.lacocinacompose.screens.Meal_List
import com.example.lacocinacompose.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.himanshoe.kalendar.common.KalendarSelector
import com.himanshoe.kalendar.common.KalendarStyle
import com.himanshoe.kalendar.common.theme.KalendarShape
import com.himanshoe.kalendar.ui.Kalendar
import com.himanshoe.kalendar.ui.KalendarType
import java.time.LocalDate
import java.util.*

@Composable
fun PickDateDialog(value: String, setShowDialog: (Boolean) -> Unit, food: String?) {


    val showKaledar = remember { mutableStateOf(true) }
    val date = remember {mutableStateOf(LocalDate.now())  }
    val meal = remember { mutableStateOf("") }
    if (showKaledar.value) {
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
                                text = "¿Que día lo vas a comer?",

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


                        Kalendar(
                            kalendarType = KalendarType.Firey(),
                            onCurrentDayClick = { day, event ->
                                date.value = LocalDate.of(day.year, day.month, day.dayOfMonth)

                            },
                            errorMessage = {
                                Log.i("ERROR_Kalendar", "ERROR")
                            },

                            kalendarStyle = KalendarStyle(
                                shape = KalendarShape.CircularShape,
                                elevation = 0.dp,
                                kalendarColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                                kalendarBackgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                                kalendarSelector = KalendarSelector.Circle(
                                    defaultColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                                    eventTextColor = Action_color_10,
                                    selectedColor = selectedDayKalendar


                                )
                            )


                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Button(
                                onClick = {
                                    showKaledar.value = false

                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10),
                                shape = RoundedCornerShape(50.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(text = "Siguiente")
                            }
                        }


                    }
                }
            }

        }
    } else {
        val range = 0.0f..100.0f
        val steps = 3
        var sliderSelection by remember { mutableStateOf(range.start) }
        var selectionNumber by remember { mutableStateOf(range.start.toInt().toString()) }
        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.Center)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            Text(
                                text = "¿Cuando lo vas a comer?",color = if (isSystemInDarkTheme()) Color.White else Color.Black
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
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,

                            ) {
                            if (selectionNumber == "0") {
                                Text(text = "Para Desayunar", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                                meal.value = "DES"
                            }
                            if (selectionNumber == "25") {
                                Text(text = "Para el Almuerzo", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                                meal.value = "ALM"
                            }
                            if (selectionNumber == "50") {
                                Text(text = "Para Comer", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                                meal.value = "COM"
                            }
                            if (selectionNumber == "75") {
                                Text(text = "Para la Merienda", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                                meal.value = "MER"
                            }
                            if (selectionNumber == "100") {
                                Text(text = "Para Cenar", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                                meal.value = "CEN"
                            }
                        }





                        Spacer(modifier = Modifier.height(10.dp))

                        Slider(
                            value = sliderSelection,
                            valueRange = range,
                            steps = steps,
                            onValueChange = { sliderSelection = it },
                            onValueChangeFinished = {
                                selectionNumber = sliderSelection.toInt().toString()
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Action_color_10,
                                activeTrackColor = Action_color_10.copy(alpha = 0.56f),
                                inactiveTrackColor = Color.LightGray.copy(alpha = 0.24f),
                                activeTickColor = Secondary_30,
                                inactiveTickColor = Action_color_10
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        val db = Firebase.firestore
                        val context = LocalContext.current
                        val dataStore = StoreUser(context)
                        val userEmail = dataStore.getEmail.collectAsState(initial = "")
                        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                            Button(
                                onClick = {
                                    Meal_List = mealExists(db,date,userEmail,meal)

                                    var exists = false
                                    for(food in Meal_List){
                                        Log.i("datos", food.toString())
                                        if(food.meal == meal.value){
                                            exists = true
                                        }
                                    }
                                    if(exists){
                                        Toast.makeText(
                                            context,
                                            "Este dia ya tiene una receta añadida",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        setShowDialog(false)

                                    }else{
                                        Log.i("datos", exists.toString())
                                        uploadMealEvent(food, date.value, meal, userEmail)
                                        setShowDialog(false)


                                    }

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

}

fun mealExists(
    db: FirebaseFirestore,
    day: MutableState<LocalDate>,
    userEmail: State<String?>,
    food: MutableState<String>
): List<MealCalendarEvent> {

    db.collection("users").document(userEmail.value.toString()).collection("events")
        .whereEqualTo("month", day.value.monthValue)
        .get()
        .addOnSuccessListener { events ->
            Meal_List = listOf()
            for (event in events) {
                val meal = event.toObject(MealCalendarEvent::class.java)
                if (meal.year == day.value.year) {
                    if (meal.month == day.value.monthValue) {
                        if (meal.day == day.value.dayOfMonth) {


                                Meal_List = Meal_List + listOf(meal)




                        }
                    }
                }

            }
        }
    return Meal_List

}

fun uploadMealEvent(
    food: String?,
    date: LocalDate,
    meal: MutableState<String>,
    userEmail: State<String?>
) {
    val id = UUID.randomUUID().toString()



    val mealCalendarEvent = MealCalendarEvent(
        id = id,
        year = date.year,
        month = date.monthValue,
        day = date.dayOfMonth,
        meal = meal.value,
        food = food.toString()
    )




    val db = Firebase.firestore
   db.collection("users").document(userEmail.value.toString()).collection("events").document(id).set(mealCalendarEvent)








}


