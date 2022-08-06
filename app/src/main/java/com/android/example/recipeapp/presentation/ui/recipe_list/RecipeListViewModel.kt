package com.android.example.recipeapp.presentation.ui.recipe_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class RecipeListViewModel
@Inject constructor(
    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String,
) : ViewModel() {

    val recipes : MutableState<List<Recipe>> = mutableStateOf(listOf())



    init {
        newSearch()
    }

    fun newSearch(){
        viewModelScope.launch {
            val result = repository.search(token = token , page = 1 , query = "chicken")
            recipes.value = result
        }
    }
}