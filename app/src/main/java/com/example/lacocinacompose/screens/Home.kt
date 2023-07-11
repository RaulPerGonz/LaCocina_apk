@file:Suppress("NAME_SHADOWING")

package com.example.lacocinacompose.screens


import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.lacocinacompose.R
import com.example.lacocinacompose.models.MealCalendarEvent
import com.example.lacocinacompose.models.Recipe
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

var Meal_List: List<MealCalendarEvent> by mutableStateOf(listOf())
var oneTimeOnly = 0
@Composable
fun HomeScreen(userName: String, userEmail: State<String?>) {
    val db = Firebase.firestore
    val currentDay = LocalDate.now()
    if(oneTimeOnly == 0){

        Meal_List = getMeals(db, currentDay, userEmail)
        oneTimeOnly++
    }
    SetHomeScreen(userName,userEmail)

}


@Composable
fun SetHomeScreen(userName: String, userEmail: State<String?>) {

    LazyColumn(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .padding(start = 20.dp, end = 20.dp, bottom = 55.dp)
            .fillMaxHeight()
    ) {
        item {
            WelcomeUser(userName = userName)
        }
        item {

            Card(
                backgroundColor = Color.Red,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(bottom = 7.dp),
                elevation = 5.dp
            ) {
                CalendarConfig(userEmail)
            }


        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(

                visible = Meal_List.isEmpty(),
                enter = expandVertically(animationSpec = tween(
                    durationMillis = 800
                )),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 800
                    )
                )
            ) {

                Column {
                    Image(
                        painter = painterResource(id = R.drawable.no_recipes),
                        contentDescription = "no recipes"
                    )
                    Text(
                        text = "No hay recetas guardadas para hoy", color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        style = MaterialTheme.typography.h3
                    )
                }


            }

        }

        items(Meal_List.size) { index ->



            AnimatedVisibility(

                visible = Meal_List[index].meal == "DES",
                enter = expandVertically(),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            ) {
                SetMealCard(
                    name = "DESAYUNO",
                    painter = painterResource(id = R.drawable.breakfast2),
                    color = Primary_60

                )
            }

            AnimatedVisibility(

                visible = Meal_List[index].meal == "ALM",
                enter = expandVertically(),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 1000
                    )
                )
            ) {
                SetMealCard(
                    name = "ALMUERZO",
                    painter = painterResource(id = R.drawable.breakfast),
                    color = Dark_Jungle_Green

                )
            }


        }

        item {
            for (meal in Meal_List) {
                if (meal.meal == "COM") {
                    SetMealCard(
                        name = "COMIDA",
                        painter = painterResource(id = R.drawable.meal),
                        color = Primary_60

                    )
                }
            }

        }
        item {
            for (meal in Meal_List) {
                if (meal.meal == "MER") {
                    SetMealCard(
                        name = "MERIENDA",
                        painter = painterResource(id = R.drawable.snack),
                        color = Primary_60

                    )
                }
            }

        }
        item {
            for (meal in Meal_List) {
                if (meal.meal == "CEN") {
                    SetMealCard(
                        name = "CENA",
                        painter = painterResource(id = R.drawable.lunch_late),
                        color = Primary_60

                    )
                }
            }

        }


    }


}


private fun getMeals(
    db: FirebaseFirestore,
    day: LocalDate,
    userEmail: State<String?>
): List<MealCalendarEvent> {

    db.collection("users").document(userEmail.value.toString()).collection("events")
        .whereEqualTo("month", day.monthValue)
        .get()
        .addOnSuccessListener { events ->
            Meal_List = listOf()
            for (event in events) {
                val meal = event.toObject(MealCalendarEvent::class.java)
                if (meal.year == day.year) {
                    if (meal.month == day.monthValue) {
                        if (meal.day == day.dayOfMonth) {
                            Meal_List = Meal_List + listOf(meal)
                        }
                    }
                }

            }
        }



    return Meal_List
}

@Composable
fun CalendarConfig(userEmail: State<String?>) {

    val db = Firebase.firestore


    Kalendar(
        kalendarType = KalendarType.Oceanic(),
        onCurrentDayClick = { day, event ->

            Meal_List = getMeals(db, day, userEmail)

        },
        errorMessage = {
            Log.i("ERROR_Kalendar", "ERROR")
        },

        kalendarStyle = KalendarStyle(
            shape = KalendarShape.CircularShape,
            elevation = 0.dp,
            kalendarColor = if (isSystemInDarkTheme()) Platinum else Color.White,
            kalendarBackgroundColor = if (isSystemInDarkTheme()) Platinum else MaterialTheme.colors.background,
            kalendarSelector = KalendarSelector.Circle(
                todayColor = selectedDayKalendarDefault,
                eventTextColor = Action_color_10,
                selectedColor = if (isSystemInDarkTheme()) Shiny_Shamrock else selectedDayKalendar,
                defaultColor = if (isSystemInDarkTheme()) Platinum else Color.White,


            )
        )


    )


}

@Composable
private fun SetMealCard(
    name: String,
    painter: Painter,
    color: Color
) {
    val context = LocalContext.current
    val db = Firebase.firestore
    Card(
        backgroundColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .clickable(onClick = {

                for (meal in Meal_List) {
                    if (meal.meal == "DES" && name.equals("DESAYUNO")) {
                        db
                            .collection("recipes")
                            .get()
                            .addOnSuccessListener { recipes ->
                                for (recipe in recipes) {
                                    val recipe = recipe.toObject(Recipe::class.java)
                                    if (recipe.id.equals(meal.food)) {
                                        val intent =
                                            Intent(context, RecipeScreenActivity()::class.java)
                                        intent.putExtra(
                                            "recipe_id",
                                            recipe.id
                                        )
                                        intent.putExtra("recipe_name", recipe.title)
                                        intent.putExtra("recipe_descript", recipe.descript)
                                        intent.putExtra("recipe_cooking_time", recipe.cooking_time)
                                        intent.putExtra(
                                            "recipe_image",
                                            recipe.image
                                        )
                                        intent.putExtra("is_added", true)
                                        context.startActivity(intent)
                                    }


                                }
                            }


                    }
                    if (meal.meal.equals("ALM") && name.equals("ALMUERZO")) {
                        db
                            .collection("recipes")
                            .get()
                            .addOnSuccessListener { recipes ->
                                for (recipe in recipes) {
                                    val recipe = recipe.toObject(Recipe::class.java)
                                    if (recipe.id.equals(meal.food)) {
                                        val intent =
                                            Intent(context, RecipeScreenActivity()::class.java)
                                        intent.putExtra(
                                            "recipe_id",
                                            recipe.id
                                        )
                                        intent.putExtra("recipe_name", recipe.title)
                                        intent.putExtra("recipe_descript", recipe.descript)
                                        intent.putExtra("recipe_cooking_time", recipe.cooking_time)
                                        intent.putExtra(
                                            "recipe_image",
                                            recipe.image
                                        )
                                        intent.putExtra("is_added", true)
                                        context.startActivity(intent)
                                    }


                                }
                            }
                    }
                    if (meal.meal.equals("COM") && name.equals("COMIDA")) {
                        db
                            .collection("recipes")
                            .get()
                            .addOnSuccessListener { recipes ->
                                for (recipe in recipes) {
                                    val recipe = recipe.toObject(Recipe::class.java)
                                    if (recipe.id.equals(meal.food)) {
                                        val intent =
                                            Intent(context, RecipeScreenActivity()::class.java)
                                        intent.putExtra(
                                            "recipe_id",
                                            recipe.id
                                        )
                                        intent.putExtra("recipe_name", recipe.title)
                                        intent.putExtra("recipe_descript", recipe.descript)
                                        intent.putExtra("recipe_cooking_time", recipe.cooking_time)
                                        intent.putExtra(
                                            "recipe_image",
                                            recipe.image
                                        )
                                        intent.putExtra("is_added", true)
                                        context.startActivity(intent)
                                    }


                                }
                            }
                    }
                    if (meal.meal.equals("MER") && name.equals("MERIENDA")) {
                        db
                            .collection("recipes")
                            .get()
                            .addOnSuccessListener { recipes ->
                                for (recipe in recipes) {
                                    val recipe = recipe.toObject(Recipe::class.java)
                                    if (recipe.id.equals(meal.food)) {
                                        val intent =
                                            Intent(context, RecipeScreenActivity()::class.java)
                                        intent.putExtra(
                                            "recipe_id",
                                            recipe.id
                                        )
                                        intent.putExtra("recipe_name", recipe.title)
                                        intent.putExtra("recipe_descript", recipe.descript)
                                        intent.putExtra("recipe_cooking_time", recipe.cooking_time)
                                        intent.putExtra(
                                            "recipe_image",
                                            recipe.image
                                        )
                                        intent.putExtra("is_added", true)
                                        context.startActivity(intent)
                                    }


                                }
                            }
                    }
                    if (meal.meal.equals("CEN") && name.equals("CENA")) {
                        db
                            .collection("recipes")
                            .get()
                            .addOnSuccessListener { recipes ->
                                for (recipe in recipes) {
                                    val recipe = recipe.toObject(Recipe::class.java)
                                    if (recipe.id.equals(meal.food)) {
                                        val intent =
                                            Intent(context, RecipeScreenActivity()::class.java)
                                        intent.putExtra(
                                            "recipe_id",
                                            recipe.id
                                        )
                                        intent.putExtra("recipe_name", recipe.title)
                                        intent.putExtra("recipe_descript", recipe.descript)
                                        intent.putExtra("recipe_cooking_time", recipe.cooking_time)
                                        intent.putExtra(
                                            "recipe_image",
                                            recipe.image
                                        )
                                        intent.putExtra("is_added", true)
                                        context.startActivity(intent)
                                    }


                                }
                            }
                    }
                }


            }),

        shape = RoundedCornerShape(20.dp),
        elevation = 5.dp
    ) {
        MealCardContent(name, painter, color)
    }
}

@Composable
fun MealCardContent(
    name: String,
    painter: Painter,
    color: Color,

    ) {


    Box(modifier = Modifier.height(200.dp)) {

        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = name,
            modifier = Modifier.padding(15.dp, top = 23.dp),
            style = MaterialTheme.typography.h5,
            color = color
        )

    }
}

@Composable
fun WelcomeUser(modifier: Modifier = Modifier, userName: String) {
    GetUserImage()
    Row(
        modifier
            .padding(vertical = 25.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(75.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            val painter =
                rememberImagePainter(data = image)

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(bottomEnd = 0.dp, bottomStart = 0.dp)),
                contentScale = ContentScale.Crop
            )

        }

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Hola $userName", fontWeight = FontWeight.Bold, fontSize = 35.sp, color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("Bienvenido", style = MaterialTheme.typography.body2, color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            }
        }
    }
}

