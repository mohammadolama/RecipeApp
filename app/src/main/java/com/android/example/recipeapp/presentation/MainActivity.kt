package com.android.example.recipeapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.android.example.recipeapp.presentation.navigation.Screen
import com.android.example.recipeapp.presentation.ui.recipe.RecipeDetailScreen
import com.android.example.recipeapp.presentation.ui.recipe.RecipeDetailViewModel
import com.android.example.recipeapp.presentation.ui.recipe_list.RecipeListScreen
import com.android.example.recipeapp.presentation.ui.recipe_list.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val recipeListViewModel: RecipeListViewModel by viewModels()
    val recipeViewModel: RecipeDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val snackbarController = SnackbarController(lifecycleScope)


        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.RecipeList.route
            ) {
                composable(
                    route = Screen.RecipeList.route,
                ) { _ ->
                    RecipeListScreen(
                        isDark = (application as BaseApplication).isDark.value,
                        onToggleTheme = (application as BaseApplication)::toggleLightTheme,
                        viewModel = recipeListViewModel,
                        snackbarController = snackbarController,
                        onNavigateToRecipeDetailScreen = {
                            navController.navigate(it)
                        }
                    )
                }

                composable(
                    route = Screen.RecipeDetail.route + "/{recipeId}",
                    arguments = listOf(
                        navArgument("recipeId") {
                            type = NavType.IntType
                        }
                    )
                ) { nav ->
                    RecipeDetailScreen(
                        isDark = (application as BaseApplication).isDark.value,
                        recipeId = nav.arguments?.getInt("recipeId"),
                        viewModel = recipeViewModel,
                    )


                }

            }
        }

    }

    override fun onBackPressed() {
        recipeViewModel.onLoad.value = false
        recipeViewModel.recipe.value = null
        super.onBackPressed()
    }
}