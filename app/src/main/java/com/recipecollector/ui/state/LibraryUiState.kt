package com.recipecollector.ui.state

import com.recipecollector.data.model.Recipe

data class LibraryUiState(
    val recipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val showFavoritesOnly: Boolean = false,
    val error: String? = null
)

sealed class LibraryUiEvent {
    data class SearchQueryChanged(val query: String) : LibraryUiEvent()
    data class CategorySelected(val category: String?) : LibraryUiEvent()
    data class FavoriteToggled(val recipeId: String, val isFavorite: Boolean) : LibraryUiEvent()
    data object ToggleFavoritesFilter : LibraryUiEvent()
    data object ClearSearch : LibraryUiEvent()
    data object Refresh : LibraryUiEvent()
}