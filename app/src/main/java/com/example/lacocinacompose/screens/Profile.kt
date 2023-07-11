package com.example.lacocinacompose.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.lacocinacompose.Screen
import com.example.lacocinacompose.login.StoreUser
import com.example.lacocinacompose.ui.theme.Action_color_10
import com.example.lacocinacompose.ui.theme.Dark_Jungle_Green
import com.example.lacocinacompose.ui.theme.Primary_60
import com.example.lacocinacompose.ui.theme.Secondary_30
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

var image by mutableStateOf("")

@Composable
fun ProfileScreen(navController: NavHostController) {
    GetUserImage()
    SetProfileScreen(navController)
}

@Composable
fun SetProfileScreen(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = StoreUser(context)
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            selectedImage = uri
        }


    val userEmail = dataStore.getEmail.collectAsState(initial = "")

    val openDialog = remember { mutableStateOf(false) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Primary_60,
            title = {
                Text(text = "¿Seguro que quieres salir?", color = if (isSystemInDarkTheme()) Color.White else Color.Black)
            },

            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = Action_color_10),

                        onClick = {
                            FirebaseAuth.getInstance().signOut()

                            coroutineScope.launch {
                                dataStore.saveEmail("")
                                dataStore.savePass("")
                                navController.navigate(Screen.Login.route)
                            }
                        }
                    ) {
                        Text("Cerrar Sesion")
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
Box(modifier = Modifier.padding(),) {
    Scaffold(
        backgroundColor = if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30,

        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                modifier = Modifier.padding(top = 20.dp),
                actions = {
                    IconButton(onClick = {
                        openDialog.value = true



                    }) {
                        Icon(Icons.Filled.ExitToApp, null)
                    }
                }
            )
        },
    ) {


        Column(
            Modifier
                .background(if (isSystemInDarkTheme()) Dark_Jungle_Green else Secondary_30)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Surface(
                modifier = Modifier.size(150.dp),
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

            val showResult = false
            selectedImage = photoGalleryPiker(bitmap, context, selectedImage, showResult) {
                launcher.launch("image/jpeg")
            }
            if (selectedImage != null) {
                uploadUserProfile(selectedImage!!, userEmail)
                Toast.makeText(
                    context,
                    "Imagen de perfil actualizada",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }
    }
}

}


fun uploadUserProfile(selectedImage: Uri, userEmail: State<String?>) {
    val formatter = SimpleDateFormat("yyyy_MM_dd_mm_ss", Locale.getDefault())
    val now = Date()
    val filename = formatter.format(now)
    val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")


    storageReference.putFile(selectedImage).addOnSuccessListener {
        val result = it.metadata!!.reference!!.downloadUrl

        result.addOnSuccessListener {

            val imageURL = it.toString()
            uploadImageUser(imageURL, userEmail.value)

        }


    }
}

fun uploadImageUser(imageURL: String, userEmail: String?) {
    val db = Firebase.firestore

    if (userEmail != null) {
        db.collection("users").document(userEmail).update("image", imageURL)
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}

@Composable
fun GetUserImage() {
    val db = Firebase.firestore
    val context = LocalContext.current
    val dataStore = StoreUser(context)
    val userEmail = dataStore.getEmail.collectAsState(initial = "")

    db.collection("users")
        .get()
        .addOnSuccessListener { users ->
            for (user in users) {
                val user = user.toObject(com.example.lacocinacompose.models.User::class.java)
                if (user.email == userEmail.value) {
                    image = user.image
                }
            }
        }
        .addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }
}

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(rememberNavController())
}