package com.example.lacocinacompose.respositories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lacocinacompose.DataOrException
import com.example.lacocinacompose.models.Ingredient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    private val repository: IngredientsRepository
): ViewModel() {
    var loading = mutableStateOf(false)
    val data: MutableState<DataOrException<List<Ingredient>, Exception>> = mutableStateOf(
        DataOrException(
            listOf(),
            Exception("")
        )
    )

    init {
        getIngredients()
    }

    private fun getIngredients() {
        viewModelScope.launch {
            loading.value = true
            data.value = repository.getProductsFromFirestore()
            loading.value = false
        }
    }
}