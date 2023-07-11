package com.example.lacocinacompose.di

import com.example.lacocinacompose.utils.Constants.INGREDIENTS_COLLECTION
import com.example.lacocinacompose.utils.Constants.NAME_PROPERTY
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

/**
 * Recoge las recetas en orden alfabético
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQueryIngredientsByName() = FirebaseFirestore.getInstance()
        .collection(INGREDIENTS_COLLECTION)
        .orderBy(NAME_PROPERTY, ASCENDING)




}

