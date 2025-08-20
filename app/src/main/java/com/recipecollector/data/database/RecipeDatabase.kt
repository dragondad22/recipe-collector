package com.recipecollector.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.recipecollector.data.database.converters.TypeConverters as RecipeTypeConverters
import com.recipecollector.data.database.dao.*
import com.recipecollector.data.model.*

@Database(
    entities = [
        Recipe::class,
        Ingredient::class,
        Step::class,
        Photo::class,
        Nutrition::class,
        DensityMap::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RecipeTypeConverters::class)
abstract class RecipeDatabase : RoomDatabase() {
    
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun stepDao(): StepDao
    abstract fun photoDao(): PhotoDao
    abstract fun nutritionDao(): NutritionDao
    abstract fun densityMapDao(): DensityMapDao
    
    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null
        
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}