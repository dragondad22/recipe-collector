package com.recipecollector.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recipecollector.ui.components.RecipeCard
import com.recipecollector.ui.components.SearchBar
import com.recipecollector.ui.state.LibraryUiEvent
import com.recipecollector.ui.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onRecipeClick: (String) -> Unit,
    onCaptureClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Collector") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(LibraryUiEvent.ToggleFavoritesFilter) }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Favorites",
                            tint = if (uiState.showFavoritesOnly) MaterialTheme.colorScheme.primary 
                                   else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCaptureClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Capture Recipe")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onEvent(LibraryUiEvent.SearchQueryChanged(it)) },
                onClearClick = { viewModel.onEvent(LibraryUiEvent.ClearSearch) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                uiState.recipes.isEmpty() -> {
                    EmptyState(
                        showFavoritesOnly = uiState.showFavoritesOnly,
                        onCaptureClick = onCaptureClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.recipes) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id) },
                                onFavoriteClick = { 
                                    viewModel.onEvent(
                                        LibraryUiEvent.FavoriteToggled(recipe.id, !recipe.favorite)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            
            uiState.error?.let { error ->
                LaunchedEffect(error) {
                    // Show snackbar or handle error
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    showFavoritesOnly: Boolean,
    onCaptureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (showFavoritesOnly) {
                "No favorite recipes yet"
            } else {
                "No recipes yet"
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (showFavoritesOnly) {
                "Mark recipes as favorites to see them here"
            } else {
                "Capture your first recipe to get started"
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (!showFavoritesOnly) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onCaptureClick,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Capture Recipe")
            }
        }
    }
}