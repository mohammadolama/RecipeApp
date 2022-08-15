package com.android.example.recipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.interactors.recipe_list.RestoreRecipes
import com.android.example.recipeapp.interactors.recipe_list.SearchRecipes
import com.android.example.recipeapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"


@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val searchRecipes: SearchRecipes,
    private val restoreRecipes: RestoreRecipes,
    @Named("auth_token") private val token: String,
    private val savedStateHandle: SavedStateHandle,


    ) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())

    val query = mutableStateOf("")

    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)

    var categoryScrollPosition: Int = 0
    var categoryScrollPosition2: Int = 0

    val loading = mutableStateOf(false)

    val page = mutableStateOf(1)

    private var recipeListScrollPosition = 0


    init {

        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            Log.d(TAG, "restoring page: $p")
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            Log.d(TAG, "restoring scroll position: $p")
            setListScrollPosition(p)
        }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        if (recipeListScrollPosition != 0) {
            onTriggerEvent(RecipeListEvent.RestoreStateEvent)
        } else {
            onTriggerEvent(RecipeListEvent.NewSearchEvent)
        }

    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {

                when (event) {
                    is RecipeListEvent.NewSearchEvent -> {
                        newSearch()
                    }
                    is RecipeListEvent.NextPageEvent -> {
                        nextPage()
                    }
                    is RecipeListEvent.RestoreStateEvent -> {
                        restoreState()
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, "onTriggerEvent: Exception: $e , ${e.cause}")
            }
        }
    }

    private fun restoreState() {

        restoreRecipes
            .excute(page = page.value, query = query.value)
            .onEach { dataState ->
                loading.value = dataState.loading
                dataState.data?.let { list ->
                    recipes.value = list
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "restore State: ${error}")
//                    TODO("ADADADA")
                }
            }.launchIn(viewModelScope)
    }

    private fun newSearch() {

        resetSearchState()
        searchRecipes
            .excute(token = token, page = page.value, query = query.value)
            .onEach { dataState ->
                loading.value = dataState.loading
                dataState.data?.let { list ->
                    recipes.value = list
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "new Search: ${error}")
//                    TODO("ADADADA")
                }
            }.launchIn(viewModelScope)


    }


    private suspend fun nextPage() {
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "NEXT PAGE: triggered: ${page.value}")

            if (page.value > 1) {
                searchRecipes
                    .excute(token = token, page = page.value, query = query.value)
                    .onEach { dataState ->
                        loading.value = dataState.loading
                        dataState.data?.let { list ->
                            appendRecipes(list)
                        }

                        dataState.error?.let { error ->
                            Log.e(TAG, "NEXT PAGE: ${error}")
//                            TODO("ADADADA")
                        }
                    }.launchIn(viewModelScope)
            }
        }

    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }


    private fun incrementPage() {
        page.value = page.value + 1
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position)
    }

    private fun resetSearchState() {
        recipes.value = listOf()
        setPage(1)
        setListScrollPosition(0)
        if (selectedCategory.value?.value != query.value) clearSelectedCategory()
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
    }


    fun onQueryChanged(query: String) {
        setQuery(query)
    }


    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }


    fun onChangedCategoryScrollPosition(position: Int, position2: Int) {
        categoryScrollPosition = position
        categoryScrollPosition2 = position2
    }


    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle[STATE_KEY_LIST_POSITION] = position
    }

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle[STATE_KEY_PAGE] = page
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle[STATE_KEY_SELECTED_CATEGORY] = category
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle[STATE_KEY_QUERY] = query
    }


}