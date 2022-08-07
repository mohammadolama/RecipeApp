package com.android.example.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.example.recipeapp.presentation.components.CircularIndeterminateProgressBar
import com.android.example.recipeapp.presentation.components.FoodCategoryChip
import com.android.example.recipeapp.presentation.components.RecipeCard
import com.android.example.recipeapp.presentation.components.SearchAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    val viewModel: RecipeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = ComposeView(requireContext())

        view.apply {
            setContent {

                val recipes = viewModel.recipes.value

                val query = viewModel.query.value

                val focusManager = LocalFocusManager.current

                val selectedCategory = viewModel.selectedCategory.value

                val scope = rememberCoroutineScope()

                val loading = viewModel.loading.value

                Column() {

                    SearchAppBar(
                        query = query,
                        onQueryChanged = viewModel::onQueryChanged,
                        newSearch = viewModel::newSearch,
                        focusManager = focusManager,
                        scrollPosition = viewModel.categoryScrollPosition,
                        scrollOffset = viewModel.categoryScrollPosition2,
                        scope = scope,
                        selectedCategory = selectedCategory,
                        onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                        onChangedCategoryScrollPosition = viewModel::onChangedCategoryScrollPosition
                    )

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn {
                            itemsIndexed(
                                items = recipes
                            ) { index, item ->
                                RecipeCard(recipe = item, onClick = {})
                            }
                        }
                        CircularIndeterminateProgressBar(isDisplayed = loading)
                    }

                }

            }
        }

        return view
    }
}