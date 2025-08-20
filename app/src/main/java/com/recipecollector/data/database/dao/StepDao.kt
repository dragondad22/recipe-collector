package com.recipecollector.data.database.dao

import androidx.room.*
import com.recipecollector.data.model.Step

@Dao
interface StepDao {
    
    @Query("SELECT * FROM steps WHERE recipeId = :recipeId ORDER BY stepNumber")
    suspend fun getStepsForRecipe(recipeId: String): List<Step>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: Step)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<Step>)
    
    @Update
    suspend fun updateStep(step: Step)
    
    @Delete
    suspend fun deleteStep(step: Step)
    
    @Query("DELETE FROM steps WHERE recipeId = :recipeId")
    suspend fun deleteStepsForRecipe(recipeId: String)
}