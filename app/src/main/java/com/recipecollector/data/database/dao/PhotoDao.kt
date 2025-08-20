package com.recipecollector.data.database.dao

import androidx.room.*
import com.recipecollector.data.model.Photo

@Dao
interface PhotoDao {
    
    @Query("SELECT * FROM photos WHERE recipeId = :recipeId")
    suspend fun getPhotosForRecipe(recipeId: String): List<Photo>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: Photo)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)
    
    @Update
    suspend fun updatePhoto(photo: Photo)
    
    @Delete
    suspend fun deletePhoto(photo: Photo)
    
    @Query("DELETE FROM photos WHERE recipeId = :recipeId")
    suspend fun deletePhotosForRecipe(recipeId: String)
}