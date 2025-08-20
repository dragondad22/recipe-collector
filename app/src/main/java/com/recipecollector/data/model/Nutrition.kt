package com.recipecollector.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "nutrition",
    foreignKeys = [
        ForeignKey(
            entity = Recipe::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Serializable
data class Nutrition(
    @PrimaryKey val recipeId: String,
    val kcal: Float? = null,
    val proteinG: Float? = null,
    val fatG: Float? = null,
    val carbG: Float? = null
)

@Serializable
data class NutritionPerServing(
    val kcal: Float? = null,
    val proteinG: Float? = null,
    val fatG: Float? = null,
    val carbG: Float? = null
)