package com.android.example.recipeapp.presentation.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.example.recipeapp.R
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.presentation.BaseApplication
import com.android.example.recipeapp.presentation.components.shimmer.LoadingShimmerList
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.android.example.recipeapp.presentation.ui.recipe_list.PAGE_SIZE
import com.android.example.recipeapp.presentation.ui.recipe_list.RecipeListEvent
import kotlinx.coroutines.launch

@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerEvent: (RecipeListEvent) -> Unit,
    scaffoldState: ScaffoldState,
    snackbarController: SnackbarController,
    navController: NavController,
    application: BaseApplication
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        if (loading && recipes.isEmpty()) {
            LoadingShimmerList(cardHeight = 250.dp, padding = 8.dp, application = application)
        }else if ((recipes.isEmpty())){
            NothingHere()
        } else {
            LazyColumn {
                itemsIndexed(
                    items = recipes
                ) { index, recipe ->
                    onChangeRecipeScrollPosition(index)
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                        onTriggerEvent(RecipeListEvent.NextPageEvent)
                    }
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            if (recipe.id != null) {
                                val bundle = Bundle()
                                bundle.putInt("recipeID", recipe.id)
                                navController.navigate(R.id.viewRecipe, bundle)
                            } else {
                                snackbarController.getScope().launch {
                                    snackbarController.showSnackbar(
                                        scaffoldState = scaffoldState,
                                        message = "Recipe Error",
                                        actionLabel = "OK"
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

    }

}