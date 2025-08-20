package com.recipecollector.domain.repository

import com.recipecollector.data.model.*
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    fun getAllRecipesWithDetails(): Flow<List<RecipeWithDetails>>
    fun searchRecipes(query: String): Flow<List<Recipe>>
    fun getRecipesByCategory(category: String): Flow<List<Recipe>>
    
    suspend fun getRecipeById(id: String): Recipe?
    suspend fun getRecipeWithDetails(id: String): RecipeWithDetails?
    
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
    suspend fun updateFavoriteStatus(id: String, favorite: Boolean)
    
    suspend fun insertIngredients(ingredients: List<Ingredient>)
    suspend fun insertSteps(steps: List<Step>)
    suspend fun insertPhotos(photos: List<Photo>)
    suspend fun insertNutrition(nutrition: Nutrition)
    
    suspend fun getDensityForIngredient(name: String): DensityMap?
}