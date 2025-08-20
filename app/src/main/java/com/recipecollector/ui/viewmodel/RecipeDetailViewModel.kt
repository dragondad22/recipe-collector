package com.recipecollector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipecollector.data.model.*
import com.recipecollector.domain.repository.RecipeRepository
import com.recipecollector.ui.state.RecipeDetailUiEvent
import com.recipecollector.ui.state.RecipeDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()
    
    fun onEvent(event: RecipeDetailUiEvent) {
        when (event) {
            is RecipeDetailUiEvent.LoadRecipe -> loadRecipe(event.recipeId)
            RecipeDetailUiEvent.ToggleFavorite -> toggleFavorite()
            RecipeDetailUiEvent.ShareRecipe -> shareRecipe()
            RecipeDetailUiEvent.EditRecipe -> { /* Handle navigation to edit */ }
            RecipeDetailUiEvent.DeleteRecipe -> deleteRecipe()
            RecipeDetailUiEvent.StartCookingMode -> { /* Handle navigation to cooking mode */ }
        }
    }
    
    private fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val recipeWithDetails = repository.getRecipeWithDetails(recipeId)
                if (recipeWithDetails != null) {
                    val bakersPercentages = calculateBakersPercentages(recipeWithDetails.ingredients)
                    val nutritionPerServing = calculateNutritionPerServing(
                        recipeWithDetails.nutrition,
                        recipeWithDetails.recipe.servings
                    )
                    
                    _uiState.update {
                        it.copy(
                            recipe = recipeWithDetails,
                            bakersPercentages = bakersPercentages,
                            nutritionPerServing = nutritionPerServing,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Recipe not found")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
    
    private fun toggleFavorite() {
        val recipe = _uiState.value.recipe?.recipe ?: return
        viewModelScope.launch {
            try {
                repository.updateFavoriteStatus(recipe.id, !recipe.favorite)
                // Reload to get updated state
                loadRecipe(recipe.id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun deleteRecipe() {
        val recipe = _uiState.value.recipe?.recipe ?: return
        viewModelScope.launch {
            try {
                repository.deleteRecipe(recipe)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    
    private fun shareRecipe() {
        // TODO: Implement email sharing functionality
    }
    
    private fun calculateBakersPercentages(ingredients: List<Ingredient>): BakersPercentages? {
        val flourIngredients = ingredients.filter { it.role == IngredientRole.FLOUR.value }
        if (flourIngredients.isEmpty()) return null
        
        val totalFlourWeight = flourIngredients.sumOf { it.gramsNum?.toDouble() ?: 0.0 }.toFloat()
        if (totalFlourWeight <= 0) return null
        
        val waterWeight = ingredients
            .filter { it.role == IngredientRole.WATER.value }
            .sumOf { it.gramsNum?.toDouble() ?: 0.0 }.toFloat()
        
        val saltWeight = ingredients
            .filter { it.role == IngredientRole.SALT.value }
            .sumOf { it.gramsNum?.toDouble() ?: 0.0 }.toFloat()
        
        val sugarWeight = ingredients
            .filter { it.role == IngredientRole.SUGAR.value }
            .sumOf { it.gramsNum?.toDouble() ?: 0.0 }.toFloat()
        
        val fatWeight = ingredients
            .filter { it.role == IngredientRole.FAT.value }
            .sumOf { it.gramsNum?.toDouble() ?: 0.0 }.toFloat()
        
        val starterWeight = ingredients
            .filter { it.role == IngredientRole.PREFERMENT.value }
            .sumOf { it.gramsNum?.toDouble() ?: 0.0 }.toFloat()
        
        return BakersPercentages(
            hydrationPct = if (waterWeight > 0) (waterWeight / totalFlourWeight) * 100 else null,
            saltPct = if (saltWeight > 0) (saltWeight / totalFlourWeight) * 100 else null,
            sugarPct = if (sugarWeight > 0) (sugarWeight / totalFlourWeight) * 100 else null,
            fatPct = if (fatWeight > 0) (fatWeight / totalFlourWeight) * 100 else null,
            starterPct = if (starterWeight > 0) (starterWeight / totalFlourWeight) * 100 else null
        )
    }
    
    private fun calculateNutritionPerServing(nutrition: Nutrition?, servings: Int): NutritionPerServing? {
        if (nutrition == null || servings <= 0) return null
        
        return NutritionPerServing(
            kcal = nutrition.kcal?.div(servings),
            proteinG = nutrition.proteinG?.div(servings),
            fatG = nutrition.fatG?.div(servings),
            carbG = nutrition.carbG?.div(servings)
        )
    }
}