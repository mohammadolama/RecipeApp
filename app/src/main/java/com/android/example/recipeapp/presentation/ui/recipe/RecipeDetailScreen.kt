package com.android.example.recipeapp.presentation.ui.recipe

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.example.recipeapp.presentation.components.RecipeView
import com.android.example.recipeapp.presentation.components.shimmer.LoadingShimmerList
import com.android.example.recipeapp.presentation.theme.AppTheme
import com.android.example.recipeapp.util.TAG

@Composable
fun RecipeDetailScreen(
    isDark: Boolean,
    recipeId: Int?,
    viewModel: RecipeDetailViewModel
) {

    Log.d(TAG  , "RecipeDetail NUMBER : ${recipeId}")
    if (recipeId == null){

    }else{
        val onload = viewModel.onLoad.value
        Log.d(TAG  , "RecipeDetail Bool : ${onload}")
        if (!onload){
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))
        }

        val loading = viewModel.loading.value
        val recipe = viewModel.recipe.value
        val scaffoldState = rememberScaffoldState()
        AppTheme(darkTheme = isDark, loading, scaffoldState) {
            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = { scaffoldState.snackbarHostState }
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    if (loading) {
                        Text(text = "Loading ...")
                        LoadingShimmerList(
                            cardHeight = 250.dp,
                            padding = 8.dp,
                            forRecipeFragment = true,
                            isDark = isDark
                        )
                    } else {
                        recipe?.let {
                            RecipeView(recipe = it)
                        }
                    }
                }
            }
        }

    }


}