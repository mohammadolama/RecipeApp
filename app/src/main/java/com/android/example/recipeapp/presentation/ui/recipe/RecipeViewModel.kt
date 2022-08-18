package com.android.example.recipeapp.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.interactors.recipe.GetRecipe
import com.android.example.recipeapp.presentation.ui.util.ConnectivityManager
import com.android.example.recipeapp.presentation.ui.util.DialogQueue
import com.android.example.recipeapp.util.MY_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


const val STATE_KEY_RECIPE = "recipe.state.recipe.key"

@HiltViewModel
class RecipeViewModel
@Inject
constructor(
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,
    private val getRecipe: GetRecipe,
    private val connectivityManager: ConnectivityManager

    ) : ViewModel() {

    val recipe: MutableState<Recipe?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    val dialogQueue = DialogQueue()

    init {
        // restore if process dies
        state.get<Int>(STATE_KEY_RECIPE)?.let { recipeId ->
            onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))
        }
    }

    fun onTriggerEvent(event: RecipeEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is RecipeEvent.GetRecipeEvent -> {
                        if (recipe.value == null) {
                            getRecipe(event.id)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(MY_TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private fun getRecipe(id: Int) {

        getRecipe.excute(
            recipeInt = id,
            token = token,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading


            dataState.data?.let { data ->
                recipe.value = data
                state.set(STATE_KEY_RECIPE, data.id)
            }

            dataState.error?.let { error ->
                Log.e(MY_TAG, "NEXT PAGE: ${error}")
                dialogQueue.appendErrorMessage(title = "Error", error)            }


        }.launchIn(viewModelScope)
    }

}