package com.recipecollector.data.database.dao

import androidx.room.*
import com.recipecollector.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    
    @Query("SELECT * FROM recipes ORDER BY createdAt DESC")
    fun getAllRecipes(): Flow<List<Recipe>>
    
    @Query("SELECT * FROM recipes WHERE favorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): Recipe?
    
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeWithDetails(id: String): RecipeWithDetails?
    
    @Transaction
    @Query("SELECT * FROM recipes ORDER BY createdAt DESC")
    fun getAllRecipesWithDetails(): Flow<List<RecipeWithDetails>>
    
    @Query("""
        SELECT * FROM recipes 
        WHERE title LIKE '%' || :query || '%' 
        OR id IN (
            SELECT recipeId FROM ingredients 
            WHERE name LIKE '%' || :query || '%'
        )
        ORDER BY createdAt DESC
    """)
    fun searchRecipes(query: String): Flow<List<Recipe>>
    
    @Query("SELECT * FROM recipes WHERE :category IN (SELECT value FROM json_each(categories))")
    fun getRecipesByCategory(category: String): Flow<List<Recipe>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)
    
    @Update
    suspend fun updateRecipe(recipe: Recipe)
    
    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
    
    @Query("UPDATE recipes SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, favorite: Boolean)
}