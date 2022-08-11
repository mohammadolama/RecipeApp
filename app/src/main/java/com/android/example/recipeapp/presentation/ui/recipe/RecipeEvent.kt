package com.android.example.recipeapp.presentation.ui.recipe

sealed class RecipeEvent {

    data class GetRecipeEvent(
        val id: Int
    ) : RecipeEvent()
}