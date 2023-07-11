package com.example.lacocinacompose

/**
 * Recurso para los textFields en los que si hay un error indica que muestre la excepción
 *
 * @param T texto introducido
 * @param E si existe excepción
 * @property data
 * @property e
 */
data class DataOrException<T, E : Exception?>(
    var data: T? = null,
    var e: E? = null
)
