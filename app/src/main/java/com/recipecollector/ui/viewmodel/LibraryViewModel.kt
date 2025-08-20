package com.recipecollector.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recipecollector.domain.repository.RecipeRepository
import com.recipecollector.ui.state.LibraryUiEvent
import com.recipecollector.ui.state.LibraryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()
    
    init {
        loadRecipes()
    }
    
    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                searchRecipes(event.query)
            }
            is LibraryUiEvent.CategorySelected -> {
                _uiState.update { it.copy(selectedCategory = event.category) }
                loadRecipesByCategory(event.category)
            }
            is LibraryUiEvent.FavoriteToggled -> {
                toggleFavorite(event.recipeId, event.isFavorite)
            }
            LibraryUiEvent.ToggleFavoritesFilter -> {
                val newShowFavorites = !_uiState.value.showFavoritesOnly
                _uiState.update { it.copy(showFavoritesOnly = newShowFavorites) }
                if (newShowFavorites) {
                    loadFavoriteRecipes()
                } else {
                    loadRecipes()
                }
            }
            LibraryUiEvent.ClearSearch -> {
                _uiState.update { it.copy(searchQuery = "", selectedCategory = null) }
                loadRecipes()
            }
            LibraryUiEvent.Refresh -> loadRecipes()
        }
    }
    
    private fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getAllRecipes().collect { recipes ->
                    _uiState.update { 
                        it.copy(recipes = recipes, isLoading = false, error = null) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, error = e.message) 
                }
            }
        }
    }
    
    private fun loadFavoriteRecipes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getFavoriteRecipes().collect { recipes ->
                    _uiState.update { 
                        it.copy(recipes = recipes, isLoading = false, error = null) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, error = e.message) 
                }
            }
        }
    }
    
    private fun searchRecipes(query: String) {
        if (query.isBlank()) {
            loadRecipes()
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.searchRecipes(query).collect { recipes ->
                    _uiState.update { 
                        it.copy(recipes = recipes, isLoading = false, error = null) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, error = e.message) 
                }
            }
        }
    }
    
    private fun loadRecipesByCategory(category: String?) {
        if (category == null) {
            loadRecipes()
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getRecipesByCategory(category).collect { recipes ->
                    _uiState.update { 
                        it.copy(recipes = recipes, isLoading = false, error = null) 
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(isLoading = false, error = e.message) 
                }
            }
        }
    }
    
    private fun toggleFavorite(recipeId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateFavoriteStatus(recipeId, isFavorite)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}