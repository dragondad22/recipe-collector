package com.recipecollector.ui.state

import android.net.Uri
import com.recipecollector.data.model.*

data class CaptureUiState(
    val capturedImageUri: Uri? = null,
    val isProcessing: Boolean = false,
    val ocrText: String? = null,
    val parsedRecipe: ParsedRecipeData? = null,
    val error: String? = null,
    val currentStep: CaptureStep = CaptureStep.Camera
)

data class ParsedRecipeData(
    val title: String = "",
    val yields: String = "",
    val servings: Int = 1,
    val category: String = "",
    val ingredients: List<ParsedIngredient> = emptyList(),
    val steps: List<String> = emptyList()
)

data class ParsedIngredient(
    val name: String,
    val quantity: Float? = null,
    val unit: String? = null,
    val grams: Float? = null
)

enum class CaptureStep {
    Camera,
    Crop,
    Processing,
    Review,
    Edit
}

sealed class CaptureUiEvent {
    data class ImageCaptured(val uri: Uri) : CaptureUiEvent()
    data class ImageCropped(val uri: Uri) : CaptureUiEvent()
    data object ProcessImage : CaptureUiEvent()
    data object RetakePhoto : CaptureUiEvent()
    data class UpdateTitle(val title: String) : CaptureUiEvent()
    data class UpdateYields(val yields: String) : CaptureUiEvent()
    data class UpdateServings(val servings: Int) : CaptureUiEvent()
    data class UpdateCategory(val category: String) : CaptureUiEvent()
    data class UpdateIngredient(val index: Int, val ingredient: ParsedIngredient) : CaptureUiEvent()
    data class UpdateStep(val index: Int, val step: String) : CaptureUiEvent()
    data object AddIngredient : CaptureUiEvent()
    data object AddStep : CaptureUiEvent()
    data class RemoveIngredient(val index: Int) : CaptureUiEvent()
    data class RemoveStep(val index: Int) : CaptureUiEvent()
    data object SaveRecipe : CaptureUiEvent()
    data object Cancel : CaptureUiEvent()
}