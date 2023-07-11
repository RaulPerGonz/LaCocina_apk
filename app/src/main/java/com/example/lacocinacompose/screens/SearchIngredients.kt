package com.example.lacocinacompose.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.presentation.components.searchbar.TextSearchBar
import com.example.lacocinacompose.DataOrException
import com.example.lacocinacompose.R
import com.example.lacocinacompose.models.Ingredient
import com.example.lacocinacompose.respositories.IngredientsViewModel
import com.example.lacocinacompose.ui.theme.*
import com.example.lacocinacompose.utils.AutoCompleteBox
import com.example.lacocinacompose.utils.AutoCompleteSearchBarTag
import com.example.lacocinacompose.utils.asAutoCompleteEntities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchIngredientsActivity : AppCompatActivity() {

    private val viewModel: IngredientsViewModel by viewModels()

    @OptIn(ExperimentalFoundationApi::class)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            LaCocinaComposeTheme {


                val listIngredients = getIngredients()
                val listIngredientsGr = getGrIngredients()
                val dataOrException = viewModel.data.value

                ProductsActivity(
                    dataOrException,
                    listIngredients, listIngredientsGr
                )
            }


        }
    }


    @OptIn(
        com.google.accompanist.permissions.ExperimentalPermissionsApi::class
    )
    @ExperimentalAnimationApi
    @Composable
    fun ProductsActivity(
        dataOrException: DataOrException<List<Ingredient>, Exception>,
        ListIngredients: MutableList<String>,
        ListIngredientsGr: MutableList<Float>
    ) {

        var ingredientsListStateName by remember { mutableStateOf(getIngredients()) }
        var ingredientsListStateGr by remember { mutableStateOf(getGrIngredients()) }

        val openDialog = remember { mutableStateOf(false) }
        val products = dataOrException.data
        val context = LocalContext.current








        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                title = {
                    Text(text = "Volver a la pantalla anterior?", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
                },
                text = {
                    Column {

                        Text("Mira bien que no se te olvide nada ", color = if (isSystemInDarkTheme()) Color.White else Color.Black)

                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10),

                            onClick = {
                                var ingredientsObjects = mutableListOf<Ingredient>()
                                if (ListIngredients.isNotEmpty()) {
                                    ingredientsObjects = ingredientsObjectList(
                                        products,
                                        ListIngredients,
                                        ingredientsListStateGr
                                    )
                                }

                                if(ingredientsObjects.isNotEmpty()){
                                    setIngredients(ingredientsObjects)
                                }





                                finish()
                            }
                        ) {
                            Text("Salir y Guardar")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Secondary_30),
                            modifier = Modifier.padding(start = 15.dp),
                            onClick = { openDialog.value = false }
                        ) {
                            Text("Cancelar", color = if (isSystemInDarkTheme()) Color.Black else Color.White)
                        }


                    }
                }
            )
        }

        Scaffold(
            modifier = Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),

            topBar = {
                TopAppBar(
                    title = { Text(text = "Añadir Ingredientes") },
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),

                    navigationIcon = {
                        IconButton(onClick = { openDialog.value = true }) {
                            Icon(Icons.Filled.ArrowBack, "backIcon")
                        }
                    },
                    backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30,
                    contentColor = Color.White,
                    elevation = 10.dp
                )
            },
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressBar(
                    isDisplayed = viewModel.loading.value

                )
            }




            products?.let {

                Column(
                    modifier = Modifier
                        .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            items(
                                items = products
                            ) { product ->
                                // ProductCard(ingredient = product)

                            }

                            item {



                                val names = products.map { it.name }
                                val gr = products.map { it.gr }
                                AutoCompleteValue(
                                    items = names,

                                    onIngredientsAddedGr = { item ->

                                        ingredientsListStateGr =
                                            (ingredientsListStateGr + listOf(
                                                gr[names.binarySearch(item)]
                                            )) as MutableList<Float>


                                    },
                                    onIngredientsAddedName = { item ->

                                        ingredientsListStateName =
                                            (ingredientsListStateName + listOf(
                                                names[names.binarySearch(item)]
                                            )) as MutableList<String>


                                    })



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



                        IngredientsList(
                            ingredientsListStateName,
                            ingredientsListStateGr,
                            ListIngredients
                        )


                    }


                }


            }

            val e = dataOrException.e
            e?.let {
                Text(
                    text = e.message!!,
                    modifier = Modifier.padding(16.dp)
                )


            }



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 15.dp, bottom = 25.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(backgroundColor = Action_color_10, onClick = {
                    val intent = Intent(context, CreateIngredientActivity()::class.java)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Add, "", tint = if (isSystemInDarkTheme()) Color.White else Color.Black)
                }
            }
        }
    }




}

fun ingredientsObjectList(
    products: List<Ingredient>?,
    ListIngredients: MutableList<String>,
    ListIngredientsGr: MutableList<Float>
): MutableList<Ingredient> {


    val names = products?.map { it.name }
    val objects = mutableListOf<Ingredient>()
    val ingedientsListObject = names?.intersect(ListIngredients)
    if (ingedientsListObject != null) {
        for (ingredientName in ingedientsListObject) {
            if (products != null) {
                for (ingredientObject in products) {
                    if (ingredientName == ingredientObject.name) {
                        objects.add(ingredientObject)
                    }
                }
            }
        }
    }




    return objects

}


@Composable
fun IngredientsList(
    ingredientsState: List<String>,
    ingredientsListStateGr: List<Float>,
    ListIngredients: MutableList<String>
) {

    val deletedIngredientsList = remember { mutableStateListOf<Int>() }
    val grams = ingredientsListStateGr.toMutableList()


   ListIngredients.clear()
    LazyColumn(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
            .fillMaxWidth(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(ingredientsState.size) { index ->


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
                        grams[index] = 0f

                        deletedIngredientsList.add(index)
                        ListIngredients.clear()

                    }
                )

                if (!deletedIngredientsList.contains(index)) {
                    ListIngredients.add(ingredientsState[index])
                }

                SwipeableActionsBox(
                    startActions = listOf(delete),

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

                            TableCell(text = ingredientsState[index])


                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight(),

                                    contentAlignment = Alignment.CenterEnd
                                ) {

                                    Card(
                                        backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                                        modifier = Modifier.width(100.dp),
                                        shape = RectangleShape
                                    ) {


                                        TableCell(text = "${grams[index]} g")


                                    }
                                }





                        }


                    }
                }
            }


        }
    }

}


@Composable
fun TableCell(
    text: String
) {


    Text(
        text = text,
        Modifier

            .padding(horizontal = 10.dp, vertical = 20.dp)
        , color = if (isSystemInDarkTheme()) Color.White else Color.Black
    )


}


@ExperimentalAnimationApi
@Composable
fun AutoCompleteValue(
    items: List<String>,
    onIngredientsAddedName: (String) -> Unit,
    onIngredientsAddedGr: (String) -> Unit
) {


    val autoCompleteEntities = items.asAutoCompleteEntities(
        filter = { item, query ->
            item.lowercase(Locale.getDefault())
                .startsWith(query.lowercase(Locale.getDefault()))
        }
    )

    AutoCompleteBox(
        items = autoCompleteEntities,
        itemContent = { item ->
            ValueAutoCompleteItem(item.value)
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { item ->


            value = item.value
            onIngredientsAddedName(value)
            onIngredientsAddedGr(value)
            filter(value)
            view.clearFocus()


        }
        val context = LocalContext.current
        TextSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag(AutoCompleteSearchBarTag),
            value = value,
            label = "Buscar Ingredientes",
            onDoneActionClick = {
                view.clearFocus()
                if (value.isNotBlank()) {

                    if (value in items) {
                        onIngredientsAddedName(value)
                        onIngredientsAddedGr(value)
                    } else {
                        val unicode = 0x1F613
                        Toast.makeText(
                            context,
                            "Este ingrediente aun no existe 😢 , ¡añadelo tu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

            },
            onClearClick = {

                value = ""
                filter(value)
                view.clearFocus()

            },
            onFocusChanged = { focusState ->
                isSearching = focusState.isFocused

            },
            onValueChanged = { query ->

                value = query
                filter(value)

            }

        )
    }
}

@Composable
fun ValueAutoCompleteItem(item: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = item, style = MaterialTheme.typography.subtitle2)
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CircularProgressBar(
    isDisplayed: Boolean
) {
    if (isDisplayed) {
        CircularProgressIndicator()
    }
}





