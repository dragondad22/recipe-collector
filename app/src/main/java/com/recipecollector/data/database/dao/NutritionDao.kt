package com.recipecollector.data.database.dao

import androidx.room.*
import com.recipecollector.data.model.Nutrition

@Dao
interface NutritionDao {
    
    @Query("SELECT * FROM nutrition WHERE recipeId = :recipeId")
    suspend fun getNutritionForRecipe(recipeId: String): Nutrition?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutrition(nutrition: Nutrition)
    
    @Update
    suspend fun updateNutrition(nutrition: Nutrition)
    
    @Delete
    suspend fun deleteNutrition(nutrition: Nutrition)
    
    @Query("DELETE FROM nutrition WHERE recipeId = :recipeId")
    suspend fun deleteNutritionForRecipe(recipeId: String)
}