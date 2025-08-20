package com.recipecollector.di

import android.content.Context
import com.recipecollector.data.database.RecipeDatabase
import com.recipecollector.data.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return RecipeDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideRecipeDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }
    
    @Provides
    fun provideIngredientDao(database: RecipeDatabase): IngredientDao {
        return database.ingredientDao()
    }
    
    @Provides
    fun provideStepDao(database: RecipeDatabase): StepDao {
        return database.stepDao()
    }
    
    @Provides
    fun providePhotoDao(database: RecipeDatabase): PhotoDao {
        return database.photoDao()
    }
    
    @Provides
    fun provideNutritionDao(database: RecipeDatabase): NutritionDao {
        return database.nutritionDao()
    }
    
    @Provides
    fun provideDensityMapDao(database: RecipeDatabase): DensityMapDao {
        return database.densityMapDao()
    }
}