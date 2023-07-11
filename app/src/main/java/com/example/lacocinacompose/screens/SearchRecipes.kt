package com.example.lacocinacompose.screens

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.lacocinacompose.R
import com.example.lacocinacompose.models.Recipe
import com.example.lacocinacompose.ui.theme.LaCocinaComposeTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchRecipesActivity : AppCompatActivity() {

    private var recipeList: List<Recipe> by mutableStateOf(listOf())

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val search: String? = intent.getStringExtra("search")
        setContent {
            LaCocinaComposeTheme {
                RecipesActivity(search)
                RecipeList()
            }



        }


    }

    @Composable
    fun RecipesActivity(search: String?) {

        GetSearch(search)


    }

    @Composable
    fun GetSearch(search: String?) {
        val db = Firebase.firestore


        db.collection("recipes")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val recipe = document.toObject(Recipe::class.java)
                    if (recipe.title.contains(search!!, ignoreCase = true)) {
                        recipeList = recipeList + listOf(recipe)


                    }


                }


            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


    }


    @Composable
    fun RecipeList() {
        LazyColumn(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            item {
                if(recipeList.isEmpty()){
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.no_recipes),
                            contentDescription = "no recipes"
                        )
                        Text(
                            text = "No existe ninguna receta con ese nombre", color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            style = MaterialTheme.typography.h3
                        )
                    }
                }
            }

            items(recipeList) { recipe ->
                RecipeCard(recipe)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Composable
    fun RecipeCard(
        recipe: Recipe
    ) {
        val context = LocalContext.current


        Card(
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .clickable {
                    val intent = Intent(context, RecipeScreenActivity()::class.java)
                    intent.putExtra("recipe_id", recipe.id)
                    intent.putExtra("recipe_name", recipe.title)
                    intent.putExtra("recipe_descript", recipe.descript)
                    intent.putExtra("recipe_cooking_time", recipe.cooking_time)
                    intent.putExtra("recipe_image", recipe.image)
                    context.startActivity(intent)
                },

            shape = RoundedCornerShape(20.dp),
            elevation = 5.dp
        ) {
            val painter =
                rememberImagePainter(data = recipe.image)
            Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {

                Image(
                    painter = painter,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(radius = 16.dp, BlurredEdgeTreatment.Rectangle),
                    contentScale = ContentScale.FillWidth
                )
                Text(text = recipe.title, modifier = Modifier.padding(15.dp),style = MaterialTheme.typography.h2, color = Color.White)

            }

        }

    }
}