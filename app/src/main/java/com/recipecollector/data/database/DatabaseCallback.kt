package com.recipecollector.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.recipecollector.data.model.DensityMap

class DatabaseCallback : RoomDatabase.Callback() {
    
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
            CoroutineScope(Dispatchers.IO).launch {
                populateDatabase(database.densityMapDao())
            }
        }
    }
    
    private suspend fun populateDatabase(densityMapDao: DensityMapDao) {
        // Seed common ingredient density mappings
        val densityMappings = listOf(
            DensityMap("all-purpose flour", 120f, 8f, 2.5f),
            DensityMap("bread flour", 120f, 8f, 2.5f),
            DensityMap("whole wheat flour", 113f, 7.5f, 2.5f),
            DensityMap("sugar", 200f, 12.5f, 4f),
            DensityMap("brown sugar", 213f, 13.5f, 4.5f),
            DensityMap("butter", 227f, 14f, 4.7f),
            DensityMap("vegetable oil", 218f, 14f, 4.5f),
            DensityMap("milk", 245f, 15f, 5f),
            DensityMap("water", 237f, 15f, 5f),
            DensityMap("salt", 292f, 18f, 6f),
            DensityMap("baking powder", 240f, 15f, 5f),
            DensityMap("cocoa powder", 85f, 5.5f, 1.8f),
            DensityMap("rolled oats", 80f, 5f, 1.7f)
        )
        
        densityMapDao.insertDensityMappings(densityMappings)
    }
    
    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null
        
        fun setInstance(instance: RecipeDatabase) {
            INSTANCE = instance
        }
    }
}