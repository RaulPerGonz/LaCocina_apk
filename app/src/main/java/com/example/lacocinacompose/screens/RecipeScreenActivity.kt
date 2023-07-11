package com.example.lacocinacompose.screens

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.lacocinacompose.models.Ingredient
import com.example.lacocinacompose.models.MealCalendarEvent
import com.example.lacocinacompose.models.Step
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Primary_60
import com.example.lacocinacompose.ui.theme.Secondary_30
import com.example.lacocinacompose.utils.LockScreenOrientation
import com.example.lacocinacompose.utils.PickDateDialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch




class RecipeScreenActivity : AppCompatActivity() {
    private var MealList: List<MealCalendarEvent> by mutableStateOf(listOf())
    private var RecipeIngredeientsList: List<Ingredient> by mutableStateOf(listOf())
    private var RecipeStepsList: List<Step> by mutableStateOf(listOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            val title: String? = intent.getStringExtra("recipe_name")
            val descript: String? = intent.getStringExtra("recipe_descript")
            val cookingTime: String? = intent.getStringExtra("recipe_cooking_time")
            val image: String? = intent.getStringExtra("recipe_image")
            val id: String? = intent.getStringExtra("recipe_id")
            val added: Boolean = intent.getBooleanExtra("is_added",false)


            RecipeScreen(title, descript, image, id,cookingTime,added)
        }

    }

    @Composable
    fun RecipeScreen(
        title: String?,
        descript: String?,
        image: String?,
        id: String?,
        cooking_time: String?,
        added: Boolean?
    ) {

        SetRecipeScreen(title, descript, image,id,cooking_time,added)
        getIngredients(id)
        getSteps(id)
    }

    private fun getIngredients(id: String?) {
        val db = Firebase.firestore
        db.collection("recipes").document(id.toString()).collection("recipe_ingredients").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val ingredient = document.toObject(Ingredient::class.java)
                    RecipeIngredeientsList = RecipeIngredeientsList + listOf(ingredient)
                }
            }
    }

    private fun getSteps(id: String?) {
        val db = Firebase.firestore
        db.collection("recipes").document(id.toString()).collection("recipe_steps").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val step = document.toObject(Step::class.java)
                    RecipeStepsList = RecipeStepsList + listOf(step)
                }
            }
    }


    @Composable
    internal fun SetRecipeScreen(
        title: String?,
        descript: String?,
        image: String?,
        id: String?,
        cooking_time: String?,
        added: Boolean?
    ) {

        RecipeImage(image,added)


        Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp), contentAlignment = Alignment.BottomCenter) {
            LazyColumn {

                item {
                    Spacer(modifier = Modifier.height(350.dp))

                }
                item {
                    RecipeHeader(title, descript,cooking_time)
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }


            }
            val openDialog = remember { mutableStateOf(false) }
            if (openDialog.value) {
                PickDateDialog(food = id,value = "", setShowDialog = {
                    openDialog.value = it
                })
            }

            if(added == false){
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier
                        .height(75.dp)
                        .weight(2.3f)
                        .padding(bottom = 22.dp, start = 13.dp, end = 13.dp),
                        onClick = {
                            openDialog.value = true
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10)
                    ) {
                        Text(text = "Añadir receta")
                    }

                }
            }


        }

    }


    enum class TabPage {
        Ingredients, Steps
    }

    val list = listOf("Ingredientes", "Pasos")


    @ExperimentalPagerApi
    @Composable
    fun TabLayoutAnimation() {
        val pagerState = rememberPagerState()
        Column(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Color.White)
                .fillMaxSize()
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(pagerState = pagerState)
        }
    }

    @Composable
    fun DoughnutChart(

        values: List<Float> = listOf(40f, 40f, 20f),
        colors: List<Color> = listOf(
            Color(0xFFFE9955),
            Color(0xFFFFC758),
            Color(0xFFC0FD6D),


            ),
        legend: List<String> = listOf("Proteina", "Carbs", "Grasa"),
        size: Dp = 150.dp,
        thickness: Dp = 25.dp,
        kcal: Double
    ) {

        // Suma todos los valores
        val sumOfValues = values.sum()

        // Calcula la proporción
        val proportions = values.map {
            it * 100 / sumOfValues
        }

        // Convierte la proporción en ángulo
        val sweepAngles = proportions.map {
            180 * it / 100
        }

        Box(contentAlignment = Alignment.Center) {
            Canvas(
                modifier = Modifier
                    .size(size = size)
            ) {

                var startAngle = 180f

                for (i in values.indices) {
                    drawArc(
                        color = colors[i],
                        startAngle = startAngle,
                        sweepAngle = sweepAngles[i],
                        useCenter = false,
                        style = Stroke(width = thickness.toPx(), cap = StrokeCap.Butt)
                    )
                    startAngle += sweepAngles[i]
                }

            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = kcal.toString())
                Text(text = "Kcal")
                Row(
                    modifier = Modifier.padding(top = 0.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in values.indices) {
                        DisplayLegend(color = colors[i], legend = legend[i])
                    }
                }

            }

        }


    }

    @Composable
    fun DisplayLegend(color: Color, legend: String) {


        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = legend,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )
        Spacer(modifier = Modifier.width(8.dp))

    }

    @Composable
    fun TabLayoutIndicator(
        tabPositions: List<TabPosition>,
        tabPage: TabPage
    ) {
        val transition = updateTransition(
            tabPage, label = "Tab Indicator"
        )
        val indicatorLeft by transition.animateDp(
            transitionSpec = {
                if (TabPage.Ingredients isTransitioningTo TabPage.Steps
                ) {
                    spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioLowBouncy
                    )
                } else {
                    spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioLowBouncy
                    )
                }
            },
            label = "Indicator Left"
        ) { page ->
            tabPositions[page.ordinal].left
        }

        val indicatorRight by transition.animateDp(
            transitionSpec = {
                if (TabPage.Ingredients isTransitioningTo TabPage.Steps
                ) {
                    spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioLowBouncy
                    )
                } else {
                    spring(
                        stiffness = Spring.StiffnessMediumLow,
                        dampingRatio = Spring.DampingRatioLowBouncy
                    )
                }
            },
            label = "Indicator Right"
        ) { page ->
            tabPositions[page.ordinal].right
        }
        val color by transition.animateColor(
            label = "Border Color"
        ) { page ->
            when (page) {
                TabPage.Ingredients -> Action_color_10
                else -> Secondary_30
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomStart)
                .offset(x = indicatorLeft)
                .width(indicatorRight - indicatorLeft)
                .padding(4.dp)
                .fillMaxSize()
                .border(
                    BorderStroke(2.dp, color), RoundedCornerShape(50.dp)
                )
        )
    }

    @ExperimentalPagerApi
    @Composable
    fun Tabs(pagerState: PagerState) {
        val scope = rememberCoroutineScope()

        val backgroundColor by animateColorAsState(
            when (pagerState.currentPage) {
                0 -> if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60
                1 -> if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60
                else -> if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60
            }
        )

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = backgroundColor,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabLayoutIndicator(
                    tabPositions = tabPositions,
                    tabPage =
                    when (pagerState.currentPage) {
                        0 -> TabPage.Ingredients
                        1 -> TabPage.Steps
                        else -> TabPage.Ingredients
                    }
                )
            }
        ) {
            list.forEachIndexed { index, _ ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = list[index],
                            color = if (pagerState.currentPage == index) Color.Black else Color.Black.copy(
                                alpha = 0.5f
                            ),
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }

    @ExperimentalPagerApi
    @Composable
    fun TabsContent(pagerState: PagerState) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            count = 2,
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> ShowIngredients()
                1 -> ShowSteps()


            }
        }
    }


    @Composable
    fun ShowIngredients() {
        Column(
            modifier = Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            for (ingredient in RecipeIngredeientsList) {
                ShowIngredient(ingredient.name, ingredient.gr)
            }
        }


    }

    @Composable
    fun ShowSteps() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            for (step in RecipeStepsList) {
                ShowStep(step.text)
            }
        }


    }


    @Composable
    fun ShowStep(step: String) {
        Row(modifier = Modifier.padding(15.dp)) {
            Text(
                step,
                style = MaterialTheme.typography.body2
            )
        }
    }


    @Composable
    fun ShowIngredient(ingredient: String, gr: Float) {

        Row(
            Modifier.background(Secondary_30),
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                elevation = 0.dp

            ) {

                TableCell(text = ingredient)

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

                        TableCell(text = "$gr g")
                    }
                }

            }


        }


    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun RecipeHeader(title: String?, descript: String?, cooking_time: String?) {

        val color = if (isSystemInDarkTheme()) Dark_Jungle_Green else (Color.White)

        Card(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = color,
            shape = RoundedCornerShape(20.dp),

        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp, vertical = 20.dp)
                        .align(Alignment.Start)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60)
                        .padding(16.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Row {
                            if (title != null) {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Row {
                            if (cooking_time != null) {
                                Icon(Icons.Filled.AccessTime, "")
                                Text(modifier = Modifier.padding(start = 5.dp), text =  "$cooking_time min", style = MaterialTheme.typography.body2)

                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            if (descript != null) {
                                Text(
                                    descript,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }


                }
                Row(
                    modifier = Modifier
                        .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val kcal = kcalCalculator()
                    val carbs = macrosCalculator("C")
                    val prot = macrosCalculator("P")
                    val fat = macrosCalculator("F")

                    val macrosList = listOf(carbs, prot, fat)
                    DoughnutChart(values = macrosList, kcal = kcal)
                }

                TabLayoutAnimation()
            }
        }


    }

    private fun macrosCalculator(macro: String): Float {
        if (macro == "C") {
            var carbs = 0.0f
            for (ingredient in RecipeIngredeientsList) {
                carbs += ingredient.carbs
            }
            return carbs
        }
        if (macro == "P") {
            var prot = 0.0f
            for (ingredient in RecipeIngredeientsList) {
                prot += ingredient.protein
            }
            return prot
        }
        if (macro == "F") {
            var fat = 0.0f
            for (ingredient in RecipeIngredeientsList) {
                fat += ingredient.fat
            }
            return fat
        }

        return 0.0f
    }


    fun kcalCalculator(): Double {
        var IngredeientsKcal = 0.0
        for (ingredient in RecipeIngredeientsList) {
            IngredeientsKcal += ingredient.kcal
        }
        return IngredeientsKcal
    }

    @Composable
    fun RecipeImage(image: String?, added: Boolean?) {

        Scaffold(modifier = Modifier.size(450.dp)) {


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
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 70.dp).clickable { finish() }
                    .clipToBounds()
                    .background(Color.Transparent)
            ) {
                Row {

                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button",
                        Modifier.padding(end = 8.dp),
                        tint = Color.White,

                    )


                    Spacer(modifier = Modifier.width(16.dp))
                    if(added == false){
                        Text(text = "Recetas", fontSize = 20.sp, color = Color.White)
                    }

                }


            }
        }

    }
}
