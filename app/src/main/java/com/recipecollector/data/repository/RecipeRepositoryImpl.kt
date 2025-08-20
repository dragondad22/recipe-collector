package com.recipecollector.data.repository

import com.recipecollector.data.database.RecipeDatabase
import com.recipecollector.data.model.*
import com.recipecollector.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepositoryImpl @Inject constructor(
    private val database: RecipeDatabase
) : RecipeRepository {
    
    private val recipeDao = database.recipeDao()
    private val ingredientDao = database.ingredientDao()
    private val stepDao = database.stepDao()
    private val photoDao = database.photoDao()
    private val nutritionDao = database.nutritionDao()
    private val densityMapDao = database.densityMapDao()
    
    override fun getAllRecipes(): Flow<List<Recipe>> = recipeDao.getAllRecipes()
    
    override fun getFavoriteRecipes(): Flow<List<Recipe>> = recipeDao.getFavoriteRecipes()
    
    override fun getAllRecipesWithDetails(): Flow<List<RecipeWithDetails>> = 
        recipeDao.getAllRecipesWithDetails()
    
    override fun searchRecipes(query: String): Flow<List<Recipe>> = 
        recipeDao.searchRecipes(query)
    
    override fun getRecipesByCategory(category: String): Flow<List<Recipe>> = 
        recipeDao.getRecipesByCategory(category)
    
    override suspend fun getRecipeById(id: String): Recipe? = recipeDao.getRecipeById(id)
    
    override suspend fun getRecipeWithDetails(id: String): RecipeWithDetails? = 
        recipeDao.getRecipeWithDetails(id)
    
    override suspend fun insertRecipe(recipe: Recipe) = recipeDao.insertRecipe(recipe)
    
    override suspend fun updateRecipe(recipe: Recipe) = recipeDao.updateRecipe(recipe)
    
    override suspend fun deleteRecipe(recipe: Recipe) = recipeDao.deleteRecipe(recipe)
    
    override suspend fun updateFavoriteStatus(id: String, favorite: Boolean) = 
        recipeDao.updateFavoriteStatus(id, favorite)
    
    override suspend fun insertIngredients(ingredients: List<Ingredient>) = 
        ingredientDao.insertIngredients(ingredients)
    
    override suspend fun insertSteps(steps: List<Step>) = stepDao.insertSteps(steps)
    
    override suspend fun insertPhotos(photos: List<Photo>) = photoDao.insertPhotos(photos)
    
    override suspend fun insertNutrition(nutrition: Nutrition) = 
        nutritionDao.insertNutrition(nutrition)
    
    override suspend fun getDensityForIngredient(name: String): DensityMap? = 
        densityMapDao.getDensityForIngredient(name)
}