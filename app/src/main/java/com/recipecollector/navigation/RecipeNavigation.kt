package com.recipecollector.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.recipecollector.ui.screen.LibraryScreen
import com.recipecollector.ui.screen.RecipeDetailScreen

@Composable
fun RecipeNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "library",
        modifier = modifier
    ) {
        composable("library") {
            LibraryScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                },
                onCaptureClick = {
                    navController.navigate("capture")
                }
            )
        }
        
        composable("recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: return@composable
            RecipeDetailScreen(
                recipeId = recipeId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditClick = {
                    navController.navigate("edit_recipe/$recipeId")
                }
            )
        }
        
        composable("capture") {
            // TODO: Implement CaptureScreen
            // CaptureScreen(
            //     onNavigateBack = { navController.popBackStack() },
            //     onRecipeSaved = { recipeId ->
            //         navController.navigate("recipe_detail/$recipeId") {
            //             popUpTo("library")
            //         }
            //     }
            // )
        }
        
        composable("edit_recipe/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: return@composable
            // TODO: Implement EditRecipeScreen
            // EditRecipeScreen(
            //     recipeId = recipeId,
            //     onNavigateBack = { navController.popBackStack() },
            //     onRecipeSaved = { navController.popBackStack() }
            // )
        }
    }
}