package com.android.example.recipeapp.presentation.ui.recipe_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RecipeListViewModel
@Inject constructor(
    private val randomString: String
) : ViewModel() {

    init {
        println("VIEWMODEL: $randomString")
    }
}