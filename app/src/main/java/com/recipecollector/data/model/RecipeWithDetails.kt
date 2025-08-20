package com.recipecollector.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithDetails(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Ingredient>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val steps: List<Step>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val photos: List<Photo>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val nutrition: Nutrition?
)