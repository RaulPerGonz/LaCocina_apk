@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.lacocinacompose.screens


import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.lacocinacompose.R
import com.example.lacocinacompose.models.Recipe
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Platinum
import com.example.lacocinacompose.ui.theme.Secondary_30
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Composable
fun SearchScreen() {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .padding(top = 30.dp)
    ) {
        item {
            var search by remember {
                mutableStateOf("")
            }
            val color = if (isSystemInDarkTheme()) TextFieldDefaults.textFieldColors(
                backgroundColor = Platinum
            ) else TextFieldDefaults.textFieldColors()


            TextField(modifier = Modifier
                .fillMaxWidth(),

                value = search,
                onValueChange = { search = it },
                label = { Text("Buscar Recetas") },
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = {
                    val intent = Intent(context, SearchRecipesActivity()::class.java)
                    intent.putExtra("search", search)
                    context.startActivity(intent)
                }), colors = color,
                leadingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
        item {
            CategoriesCard()
        }
    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoriesCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 50.dp)
    ) {


        Text(
            text = "Categorias",
            modifier = Modifier.padding(start = 15.dp),
            style = MaterialTheme.typography.h5,
            color = if (isSystemInDarkTheme()) White else Color.Black
        )
        val context = LocalContext.current
        val state = rememberPagerState()


        val seaFoodList = listOf(
            Recipe(
                "a987ad5f-36d6-47aa-a055-953d3635c366",
                "Arroz de pulpitos",
                "Cuando mi pescadero tiene en su mostrador pulpitos pequeños, me cuesta resistirme a llevármelos para poder preparar la receta que os voy a enseñar hoy. Ayer tenían, por lo que hoy hemos hecho para comer esta receta de arroz de pulpitos que tanto nos gusta a mi familia y a mí.",
                "28",
                1,
                "https://firebasestorage.googleapis.com/v0/b/la-cocina-compose.appspot.com/o/images%2F2022_06_08_11_12?alt=media&token=f0993a20-ccda-4355-8610-1a7b079be3f9",

            ),
            Recipe(
                "f748368a-39db-4a46-b098-71b914f4e445",
                "Salmón al vapor",
                "Hoy vengo con una de esas receta que merecen un hueco en vuestro recetario. Por fácil, rápida, sabrosa, saludable y mucho más, pues seguro que encontráis otros calificativos cuando probéis esta ensalada de judías y salmón al vapor.",
                "30",
                1,
                "https://firebasestorage.googleapis.com/v0/b/la-cocina-compose.appspot.com/o/images%2F2022_06_08_01_03?alt=media&token=1619c159-0c1d-4ac6-9ba8-a1d302defb98"
            )
        )

        val vegieFoodList = listOf(
            Recipe(
                "469cb992-fa40-4c72-89c9-20b11475f1ab",
                "Ensalada mimosa",
                "Si has visto esta ensalada mimosa en alguna una ocasión seguro que te acuerdas de ella. La puesta en escena es de lo más pintón y se queda grabada fácilmente en la memoria. Debe su nombre a la flor de las mimosas de un color amarillo brillante muy parecido al de la yema del huevo, uno de los ingredientes de esta receta.",
                "40",
                1,
                "https://firebasestorage.googleapis.com/v0/b/la-cocina-compose.appspot.com/o/images%2F2022_05_26_02_03?alt=media&token=b455265e-351d-4c8f-92de-bd703d0e774f"
            ),
            Recipe(
                "8d16a781-d222-45d6-bbbe-182e392f7508",
                "Berenjena rellena de arroz",
                "Si de recetas saludables hablamos, no puede faltar la fiel berenjena. Con un sabor impresionante y unos ingredientes nobles, las berenjenas rellenas con arroz no llevan tanto tiempo de hacer como parece. ¿El secreto para acortar el tiempo? La precocción de la verdura en el microondas.",
                "80",
                1,
                "https://firebasestorage.googleapis.com/v0/b/la-cocina-compose.appspot.com/o/images%2F2022_06_05_26_02?alt=media&token=14ad1f90-0485-4d42-87f1-7f748f7a943e"
            )
        )

        val meatFoodList = listOf(
            Recipe(
                "f8613f33-dcf1-49da-9af8-4ed3e5c8cdde",
                "Estofado de ternera con patatas",
                "Las recetas clásicas, esas que saben a hogar y tienen la capacidad de despertar recuerdos, son siempre bienvenidas en nuestras mesas. Una misma elaboración se puede preparar de distinta manera en cada casa, pero siempre produce el mismo resultado: a todos reconfortante ",
                "90",
                1,
                "https://firebasestorage.googleapis.com/v0/b/la-cocina-compose.appspot.com/o/images%2F2022_06_09_03_49?alt=media&token=efc4cf3a-9e7e-4673-a3cb-46fa6c36c892"
            ),
            Recipe(
                "9372545c-3985-49ab-8b72-2638f4e75a83",
                "Pechuga de pollo rellena",
                "La socorrida carne de pollo se presenta con esta receta de lo más deliciosa, y es que apetecen esta pechugas de pollo rellenas con mozarella y tomates secos, por su gran sabor y también por la facilidad con la que se preparan casi, casi sin darnos cuenta.",
                "45",
                1,
                "https://firebasestorage.googleapis.com/v0/b/la-cocina-compose.appspot.com/o/images%2F2022_06_09_12_22?alt=media&token=d5cc879e-70a5-4fa1-99e0-f54a99d8d197"
            )
        )
        HorizontalPager(count = 3, state = state) { page ->

            when (page) {
                0 -> Card(
                    backgroundColor = Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                        .clickable {
                            val intent = Intent(context, RecipeScreenActivity()::class.java)
                            val randomSeaFood = seaFoodList.random()
                            intent.putExtra("recipe_id", randomSeaFood.id)
                            intent.putExtra("recipe_name", randomSeaFood.title)
                            intent.putExtra("recipe_descript", randomSeaFood.descript)
                            intent.putExtra("recipe_cooking_time", randomSeaFood.cooking_time)
                            intent.putExtra("recipe_image", randomSeaFood.image)
                            intent.putExtra("is_added", false)
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = 5.dp
                ) {
                    Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {

                        GifImage(imageID = R.drawable.sea, modifier = Modifier.fillMaxSize())
                        Text(text = "SEA FOOD", style = MaterialTheme.typography.h5, color = White)
                    }

                }

                1 -> Card(
                    backgroundColor = Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                        .clickable {
                            val randomVegieFood = vegieFoodList.random()
                            val intent = Intent(context, RecipeScreenActivity()::class.java)
                            intent.putExtra("recipe_id", randomVegieFood.id)
                            intent.putExtra("recipe_name", randomVegieFood.title)
                            intent.putExtra("recipe_descript", randomVegieFood.descript)
                            intent.putExtra("recipe_cooking_time", randomVegieFood.cooking_time)
                            intent.putExtra("recipe_image", randomVegieFood.image)
                            intent.putExtra("is_added", false)
                            context.startActivity(intent)


                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = 5.dp
                ) {
                    Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {

                        GifImage(imageID = R.drawable.rice_field, modifier = Modifier.fillMaxSize())
                        Text(text = "VEGIE", style = MaterialTheme.typography.h5, color = White)
                    }

                }

                2 -> Card(
                    backgroundColor = Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                        .clickable {
                            val randomMeatFood = meatFoodList.random()
                            val intent = Intent(context, RecipeScreenActivity()::class.java)
                            intent.putExtra("recipe_id", randomMeatFood.id)
                            intent.putExtra("recipe_name", randomMeatFood.title)
                            intent.putExtra("recipe_descript", randomMeatFood.descript)
                            intent.putExtra("recipe_cooking_time", randomMeatFood.cooking_time)
                            intent.putExtra("recipe_image", randomMeatFood.image)
                            intent.putExtra("is_added", false)
                            context.startActivity(intent)


                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = 5.dp
                ) {
                    Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {

                        GifImage(imageID = R.drawable.cows, modifier = Modifier.fillMaxSize())
                        Text(text = "CARNIVORE", style = MaterialTheme.typography.h5, color = White)
                    }

                }
            }

        }
        Spacer(modifier = Modifier.padding(4.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DotsIndicator(
                totalDots = 3,
                selectedIndex = state.currentPage,
                selectedColor = Color.Black,
                unSelectedColor = Color.Gray
            )
        }


    }
}


@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
) {

    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()

    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    imageID: Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }
        .build()
    Image(
        painter = rememberImagePainter(
            imageLoader = imageLoader,
            data = imageID,

            ),
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
@Preview(showSystemUi = true)
fun SearchScreenPreview() {
    SearchScreen()
}



