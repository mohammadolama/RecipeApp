package com.android.example.recipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.android.example.recipeapp.presentation.components.RecipeList
import com.android.example.recipeapp.presentation.components.SearchAppBar
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.android.example.recipeapp.presentation.theme.AppTheme
import com.android.example.recipeapp.util.TAG

@Composable
fun RecipeListScreen (
    isDark : Boolean,
    onToggleTheme: () ->Unit,
    viewModel: RecipeListViewModel,
    snackbarController: SnackbarController,
    onNavigateToRecipeDetailScreen : (String) -> Unit

){

    Log.d(TAG , "RecipeListScreen: $viewModel")
    val recipes = viewModel.recipes.value

    val query = viewModel.query.value

    val focusManager = LocalFocusManager.current

    val selectedCategory = viewModel.selectedCategory.value

    val scope = rememberCoroutineScope()

    val loading = viewModel.loading.value

    val page = viewModel.page.value

    val scaffoldState = rememberScaffoldState()

    AppTheme(
        darkTheme = isDark,
        loading = loading,
        scaffoldState = scaffoldState
    ) {


        Scaffold(
            topBar = {
                SearchAppBar(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    newSearch = {
                        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
                    },

                    focusManager = focusManager,
                    scrollPosition = viewModel.categoryScrollPosition,
                    scrollOffset = viewModel.categoryScrollPosition2,
                    scope = scope,
                    selectedCategory = selectedCategory,
                    onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                    onChangedCategoryScrollPosition = viewModel::onChangedCategoryScrollPosition,
                    onToggleTheme = {
                        onToggleTheme()
                    }
                )

            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding)
            ) {
                RecipeList(
                    loading = loading,
                    recipes = recipes,
                    onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                    page = page,
                    onTriggerEvent = viewModel::onTriggerEvent,
                    scaffoldState = scaffoldState,
                    snackbarController = snackbarController,
                    isDark = isDark,
                    onNavigateToRecipeDetailScreen = onNavigateToRecipeDetailScreen
                )

            }
        }
    }
}