@file:OptIn(ExperimentalCoroutinesApi::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.lacocinacompose.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lacocinacompose.R
import com.example.lacocinacompose.login.CustomOutlinedTextField
import com.example.lacocinacompose.models.Ingredient
import com.example.lacocinacompose.models.Recipe
import com.example.lacocinacompose.models.Step
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Primary_60
import com.example.lacocinacompose.ui.theme.Secondary_30
import com.example.lacocinacompose.utils.PickGrDialog
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.text.SimpleDateFormat
import java.util.*

private var ingredientsRecipeList = mutableListOf<Ingredient>()
private var Recipe_Steps_List= mutableListOf<Step>()
private var showIngredientsTable by mutableStateOf(false)
private var showStepsTable by mutableStateOf(false)
private var name by mutableStateOf("")
private var descript by mutableStateOf("")
private var time by mutableStateOf("")
private var selectedImage by mutableStateOf<Uri?>(null)
private var isIngredientsValid by mutableStateOf(false)
private var isStepsValid by mutableStateOf(false)


@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun AddScreen() {
    val db = Firebase.firestore


    SetAddScreen(db)
}

fun setIngredients(ListIngredients: MutableList<Ingredient>) {
    if (ListIngredients != null) {
        ingredientsRecipeList = ListIngredients
        showIngredientsTable = true
        isIngredientsValid = true
    }


}

fun setSteps(ListSteps: MutableList<String>) {
    Recipe_Steps_List.clear()
    for (step in ListSteps) {
        val stp = Step(text = step, id = UUID.randomUUID().toString())
        Recipe_Steps_List = (Recipe_Steps_List + listOf(stp)) as MutableList<Step>
    }
    Log.i("seeet", Recipe_Steps_List.toString())

    if(ListSteps.isNotEmpty()){
        isStepsValid = true
    }
    showStepsTable = true




}
fun getIngredients(): MutableList<String> {
    var list = mutableListOf<String>()
   for(item in ingredientsRecipeList){
       list = (list +  listOf(item.name)) as MutableList<String>
   }
    return list

}

fun getGrIngredients(): MutableList<Float> {
    var list = mutableListOf<Float>()
    for(item in ingredientsRecipeList){
        list = (list + listOf(item.gr)) as MutableList<Float>
    }
    return list

}


fun getSteps(): MutableList<String> {
    var list = mutableListOf<String>()
    for(item in Recipe_Steps_List){
        list = (list + listOf(item.text)) as MutableList<String>
    }
    return list

}






@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun SetAddScreen(db: FirebaseFirestore) {

    val context = LocalContext.current


    val isNameValid by derivedStateOf {
        name.isNotEmpty()
    }
    val isDescriptValid by derivedStateOf {
        descript.isNotEmpty()
    }
    val isTimeValid by derivedStateOf {
        time.isNotEmpty()
    }




    AddRecipeImage()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 55.dp, top = 250.dp),
        contentAlignment = Alignment.BottomCenter
    ) {


        LazyColumn(modifier = Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Color.White)) {

            item {
                AddRecipe()
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .height(75.dp)
                    .weight(2.3f)
                    .padding(bottom = 22.dp, start = 13.dp, end = 13.dp),
                onClick = {
                    uploadPhoto(db, name, descript, time, selectedImage!!)
                    name = ""
                    descript = ""
                    time = ""
                    selectedImage = null
                    showStepsTable = false
                    showIngredientsTable = false
                    Toast.makeText(
                        context,
                        "¡Receta añadida!",
                        Toast.LENGTH_SHORT
                    ).show()

                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10),
                enabled = isNameValid && isDescriptValid && isTimeValid && isIngredientsValid && isStepsValid
            ) {
                Text(text = "Añadir receta")
            }
        }

    }


}

@Composable
fun AddRecipe() {
    val focusManager = LocalFocusManager.current
    val validateName by rememberSaveable { mutableStateOf(true) }
    val validateDescrip by rememberSaveable { mutableStateOf(true) }
    val validateTime by rememberSaveable { mutableStateOf(true) }
    val validateError = "Porfavor introduzca un valor valido"

    Card(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Color.White,
        shape = RoundedCornerShape(20.dp),


    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


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
                value = descript,
                onValueChange = { descript = it },
                label = "Descripción",
                showError = !validateDescrip,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            CustomOutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = "Duración",
                showError = !validateTime,
                errorMessage = validateError,
                leadingIconImageVector = Icons.Default.Add,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            AddTabLayout()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddTabLayout() {
    val pagerState = rememberPagerState()
    Column(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Color.White)
            .fillMaxSize()
    ) {
        AddTabs(pagerState = pagerState)
        AddTabsContent(pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddTabsContent(pagerState: PagerState) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize().background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60),
        state = pagerState,
        count = 2,
        verticalAlignment = Alignment.Top
    ) { page ->

        when (page) {
            0 -> AddIngredients()
            1 -> AddSteps()


        }
    }
}

@Composable
fun AddSteps() {
    val context = LocalContext.current
    Column {
        if (showStepsTable) {
            Box {
                Column {
                    Recipe_Steps_List.forEachIndexed { index, _ ->


                        Row(
                            Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Card(
                                backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                                modifier = Modifier.fillMaxSize(),
                                shape = RectangleShape,
                                elevation = 0.dp

                            ) {

                                TableCell(text = Recipe_Steps_List[index].text)



                            }


                        }


                    }
                }


            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {

            Button(modifier = Modifier.padding(vertical = 20.dp), onClick = {
                showStepsTable = false
                val intent = Intent(context, AddStepsActivity()::class.java)
                context.startActivity(intent)

            }) {
                Text(text = "Añadir Pasos")
            }
        }
    }

}

@Composable
fun AddIngredients() {
    val context = LocalContext.current
    var ingredientListIndex by rememberSaveable { mutableStateOf(0) }
    val openEditDialog = remember { mutableStateOf(false) }

    if (openEditDialog.value) {
        PickGrDialog(list = ingredientsRecipeList, index = ingredientListIndex, setShowDialog = {
            openEditDialog.value = it
        })
    }
    Column(modifier = Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60)) {
        if (showIngredientsTable) {
            Box {
                Column(modifier = Modifier.background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60)) {
                    ingredientsRecipeList.forEachIndexed { index, item ->

                        val edit = SwipeAction(
                            icon = painterResource(R.drawable.ic_edit),
                            background = Action_color_10,
                            isUndo = true,
                            onSwipe = {
                                ingredientListIndex = index
                                openEditDialog.value = true


                            },
                        )


                        SwipeableActionsBox(
                            startActions = listOf(edit)
                            //endActions = listOf(edit)
                        ) {
                            Row(
                                Modifier.background(Secondary_30),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Card(
                                    backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
                                    modifier = Modifier.fillMaxSize(),
                                    shape = RectangleShape,
                                    elevation = 0.dp

                                ) {

                                    TableCell(text = ingredientsRecipeList[index].name)

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

                                            TableCell(text = "${ingredientsRecipeList[index].gr} g")

                                        }
                                    }

                                }


                            }
                        }

                    }
                }


            }
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(modifier = Modifier.padding(vertical = 20.dp), onClick = {
                showIngredientsTable = false

                val intent =
                    Intent(context, SearchIngredientsActivity()::class.java)


                context.startActivity(intent)
            }) {
                Text(text = "Añadir Ingredientes")
            }
        }

    }


}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddTabs(pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    val list = listOf("Ingredientes", "Pasos")
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
                    0 -> RecipeScreenActivity.TabPage.Ingredients
                    1 -> RecipeScreenActivity.TabPage.Steps
                    else -> RecipeScreenActivity.TabPage.Ingredients
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
                        color = if (pagerState.currentPage == index) if (isSystemInDarkTheme()) Color.White else Color.Black else Color.Black.copy(
                            alpha = 0.5f
                        ),
                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

@Composable
fun TabLayoutIndicator(
    tabPositions: List<TabPosition>,
    tabPage: RecipeScreenActivity.TabPage
) {
    val transition = updateTransition(
        tabPage, label = "Tab Indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (RecipeScreenActivity.TabPage.Ingredients isTransitioningTo RecipeScreenActivity.TabPage.Steps
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
            if (RecipeScreenActivity.TabPage.Ingredients isTransitioningTo RecipeScreenActivity.TabPage.Steps
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
            RecipeScreenActivity.TabPage.Ingredients -> Action_color_10
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

@Composable
fun AddRecipeImage() {

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImage = uri
        }


    val showResult = true
    selectedImage = photoGalleryPiker(bitmap, context, selectedImage, showResult) {
        launcher.launch("image/jpeg")
    }


}


@ExperimentalPermissionsApi
fun uploadRecipe(
    db: FirebaseFirestore,
    text: String,
    descrip: String,
    time: String,
    ingredientsRecipeList: MutableList<Ingredient>,
    imageURL: String,

    ) {
    val id = UUID.randomUUID().toString()
    val recipe = Recipe(
        id = id,
        title = text,
        descript = descrip,
        cooking_time = time,
        favs = 0,
        image = imageURL
    )



    db.collection("recipes").document(id).set(recipe)
        .addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
        }
        .addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
    for (item in ingredientsRecipeList) {

        db.collection("recipes").document(id).collection("recipe_ingredients")
            .document(item.id).set(item)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document of document", e)
            }
    }
    ingredientsRecipeList.clear()

    for (item in Recipe_Steps_List) {


        db.collection("recipes").document(id).collection("recipe_steps")
            .document(item.id).set(item)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document of document", e)
            }
    }
    Recipe_Steps_List.clear()

}


@OptIn(ExperimentalPermissionsApi::class)
fun uploadPhoto(
    db: FirebaseFirestore,
    text: String,
    descrip: String,
    time: String,
    selectedImage: Uri
) {


    val formatter = SimpleDateFormat("yyyy_MM_dd_mm_ss", Locale.getDefault())
    val now = Date()
    val filename = formatter.format(now)
    val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")


    storageReference.putFile(selectedImage).addOnSuccessListener { it ->
        val result = it.metadata!!.reference!!.downloadUrl
        result.addOnSuccessListener {

            val imageURL = it.toString()
            uploadRecipe(
                db, text, descrip, time,
                ingredientsRecipeList, imageURL
            )

        }


    }

}


@Suppress("DEPRECATION")
@Composable
fun photoGalleryPiker(
    bitmap: MutableState<Bitmap?>,
    context: Context,
    selectedImage: Uri? = null,
    showResult: Boolean,
    onImageClick: () -> Unit
): Uri? {

    if (showResult) {
        if (selectedImage != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(bottomEnd = 0.dp, bottomStart = 0.dp)),
                shape = RoundedCornerShape(20.dp),
                color = Color.Gray,


                ) {
                selectedImage.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value =
                            MediaStore.Images.Media.getBitmap(context.contentResolver, it)

                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }

                    bitmap.value?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    onImageClick()
                                },
                            contentScale = ContentScale.Crop
                        )
                    }

                }

            }
        } else
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .clip(RoundedCornerShape(bottomEnd = 0.dp, bottomStart = 0.dp)),
                shape = RoundedCornerShape(20.dp),
                color = Color.Gray,


                ) {

                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color(0xFFCCCCCC),
                        contentColor = Color(0xFFF5F5F5),

                        ),
                    onClick = onImageClick
                ) {
                    Icon(Icons.Filled.Edit, "editIcon")
                }


            }


        return selectedImage
    } else {
        if (selectedImage != null) {

            selectedImage.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)

                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }


            }


        } else
            OutlinedButton(modifier = Modifier.padding(top = 130.dp), onClick = onImageClick) {
                Text(text = "Imagen")
            }

        return selectedImage
    }


}








