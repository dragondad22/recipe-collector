package com.recipecollector.data.database.dao

import androidx.room.*
import com.recipecollector.data.model.Ingredient

@Dao
interface IngredientDao {
    
    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId ORDER BY id")
    suspend fun getIngredientsForRecipe(recipeId: String): List<Ingredient>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<Ingredient>)
    
    @Update
    suspend fun updateIngredient(ingredient: Ingredient)
    
    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)
    
    @Query("DELETE FROM ingredients WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsForRecipe(recipeId: String)
}