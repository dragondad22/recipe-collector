package com.recipecollector.ui.state

import com.recipecollector.data.model.*

data class RecipeDetailUiState(
    val recipe: RecipeWithDetails? = null,
    val isLoading: Boolean = false,
    val bakersPercentages: BakersPercentages? = null,
    val nutritionPerServing: NutritionPerServing? = null,
    val error: String? = null
)

sealed class RecipeDetailUiEvent {
    data class LoadRecipe(val recipeId: String) : RecipeDetailUiEvent()
    data object ToggleFavorite : RecipeDetailUiEvent()
    data object ShareRecipe : RecipeDetailUiEvent()
    data object EditRecipe : RecipeDetailUiEvent()
    data object DeleteRecipe : RecipeDetailUiEvent()
    data object StartCookingMode : RecipeDetailUiEvent()
}