package com.recipecollector.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.recipecollector.ui.state.RecipeDetailUiEvent
import com.recipecollector.ui.viewmodel.RecipeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(recipeId) {
        viewModel.onEvent(RecipeDetailUiEvent.LoadRecipe(recipeId))
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.recipe?.recipe?.title ?: "Recipe") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(RecipeDetailUiEvent.ToggleFavorite) }) {
                        Icon(
                            if (uiState.recipe?.recipe?.favorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Toggle favorite",
                            tint = if (uiState.recipe?.recipe?.favorite == true) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { viewModel.onEvent(RecipeDetailUiEvent.ShareRecipe) }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.recipe != null -> {
                val recipe = uiState.recipe!!
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Recipe info
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (recipe.recipe.categories.isNotEmpty()) {
                                Text(
                                    text = recipe.recipe.categories.joinToString(" • "),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Yields: ${recipe.recipe.yields}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Servings: ${recipe.recipe.servings}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    // Ingredients
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Ingredients",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            
                            recipe.ingredients.forEach { ingredient ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "• ",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = buildString {
                                            ingredient.qtyNum?.let { append("${it} ") }
                                            ingredient.unit?.let { append("${it} ") }
                                            append(ingredient.name)
                                            ingredient.gramsNum?.let { append(" (${it}g)") }
                                        },
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                    
                    // Steps
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Instructions",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            
                            recipe.steps.forEach { step ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "${step.stepNumber}. ",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = step.text,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                    
                    // Baker's percentages (if available)
                    uiState.bakersPercentages?.let { bakers ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Baker's Percentages",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                bakers.hydrationPct?.let {
                                    Text("Hydration: ${String.format("%.1f", it)}%")
                                }
                                bakers.saltPct?.let {
                                    Text("Salt: ${String.format("%.1f", it)}%")
                                }
                                bakers.sugarPct?.let {
                                    Text("Sugar: ${String.format("%.1f", it)}%")
                                }
                                bakers.fatPct?.let {
                                    Text("Fat: ${String.format("%.1f", it)}%")
                                }
                                bakers.starterPct?.let {
                                    Text("Starter: ${String.format("%.1f", it)}%")
                                }
                            }
                        }
                    }
                    
                    // Nutrition (if available)
                    uiState.nutritionPerServing?.let { nutrition ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Nutrition (per serving, approximate)",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                nutrition.kcal?.let {
                                    Text("Calories: ${String.format("%.0f", it)} kcal")
                                }
                                nutrition.proteinG?.let {
                                    Text("Protein: ${String.format("%.1f", it)}g")
                                }
                                nutrition.fatG?.let {
                                    Text("Fat: ${String.format("%.1f", it)}g")
                                }
                                nutrition.carbG?.let {
                                    Text("Carbs: ${String.format("%.1f", it)}g")
                                }
                            }
                        }
                    }
                }
            }
            
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}