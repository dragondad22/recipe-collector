package com.recipecollector.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recipes")
@Serializable
data class Recipe(
    @PrimaryKey val id: String,
    val title: String,
    val yields: String,
    val servings: Int,
    val categories: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val prepTimeMin: Int? = null,
    val cookTimeMin: Int? = null,
    val totalTimeMin: Int? = null,
    val favorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class RecipeSource(
    val type: String, // "photo", "manual", etc.
    val note: String? = null
)

@Serializable
data class RecipeTimes(
    val prep: Int? = null,
    val rest: Int? = null,
    val cook: Int? = null,
    val total: Int? = null
)

@Serializable
data class BakersPercentages(
    val hydrationPct: Float? = null,
    val saltPct: Float? = null,
    val sugarPct: Float? = null,
    val fatPct: Float? = null,
    val starterPct: Float? = null
)