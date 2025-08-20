package com.recipecollector.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "ingredients",
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
data class Ingredient(
    @PrimaryKey val id: String,
    val recipeId: String,
    val name: String,
    val qtyNum: Float? = null,
    val unit: String? = null,
    val gramsNum: Float? = null,
    val role: String? = null, // flour, water, salt, sugar, fat, preferment
    val foodCode: String? = null
)

enum class IngredientRole(val value: String) {
    FLOUR("flour"),
    WATER("water"),
    SALT("salt"),
    SUGAR("sugar"),
    FAT("fat"),
    PREFERMENT("preferment")
}