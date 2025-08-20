package com.recipecollector.data.database.dao

import androidx.room.*
import com.recipecollector.data.model.DensityMap

@Dao
interface DensityMapDao {
    
    @Query("SELECT * FROM density_map WHERE name = :name")
    suspend fun getDensityForIngredient(name: String): DensityMap?
    
    @Query("SELECT * FROM density_map")
    suspend fun getAllDensityMappings(): List<DensityMap>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDensityMapping(densityMap: DensityMap)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDensityMappings(densityMaps: List<DensityMap>)
    
    @Update
    suspend fun updateDensityMapping(densityMap: DensityMap)
    
    @Delete
    suspend fun deleteDensityMapping(densityMap: DensityMap)
}