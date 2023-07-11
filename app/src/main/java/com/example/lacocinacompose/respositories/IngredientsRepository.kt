package com.example.lacocinacompose.respositories

import com.example.lacocinacompose.DataOrException
import com.example.lacocinacompose.models.Ingredient
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IngredientsRepository @Inject constructor (
    private val queryProductsByName: Query
) {
    suspend fun getProductsFromFirestore(): DataOrException<List<Ingredient>, Exception> {
        val dataOrException = DataOrException<List<Ingredient>, Exception>()
        try {
            dataOrException.data = queryProductsByName.get().await().map { document ->
                document.toObject(Ingredient::class.java)
            }
        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException
    }
}

