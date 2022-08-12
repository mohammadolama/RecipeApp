package com.android.example.recipeapp.presentation.ui.recipe_list

sealed class RecipeListEvent {

    object NewSearchEvent : RecipeListEvent()

    object NextPageEvent : RecipeListEvent()

    object RestoreStateEvent : RecipeListEvent()
}