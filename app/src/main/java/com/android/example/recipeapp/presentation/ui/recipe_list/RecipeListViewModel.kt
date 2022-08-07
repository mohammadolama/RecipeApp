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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class RecipeListViewModel
@Inject constructor(
    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String,
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())

    val query = mutableStateOf("")

    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)

    var categoryScrollPosition: Int = 0
    var categoryScrollPosition2: Int = 0

    val loading = mutableStateOf(false)

    init {
        newSearch()
    }

    fun newSearch() {
        viewModelScope.launch {
            loading.value = true
            resetSearchState()
            delay(2000)

            val result = repository.search(
                token = token,
                page = 1,
                query = query.value
            )

            recipes.value = result

            loading.value = false
        }
    }


    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangedCategoryScrollPosition(position: Int, position2: Int) {
        categoryScrollPosition = position
        categoryScrollPosition2 = position2
    }

    private fun clearSelectedCategory(){
        selectedCategory.value = null
    }

    private fun resetSearchState(){
        recipes.value = listOf()
        if (selectedCategory.value?.value != query.value){
            clearSelectedCategory()
        }
    }

}