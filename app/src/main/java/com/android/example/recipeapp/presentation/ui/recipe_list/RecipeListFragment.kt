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
import com.android.example.recipeapp.presentation.components.FoodCategoryChip
import com.android.example.recipeapp.presentation.components.RecipeCard
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

                Column() {

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 8.dp,
                        color = Color.White
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(8.dp),
                                    value = query,
                                    onValueChange = {
                                        viewModel.onQueryChanged(it)
                                    },
                                    label = {
                                        Text(text = "Search")
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Filled.Search, "d")
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Search
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onSearch = {
                                            viewModel.newSearch()
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colors.onSurface
                                    ),
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = MaterialTheme.colors.surface,
                                    )
                                )

                            }

                            val scrollState = rememberLazyListState()
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, bottom = 8.dp),
                                state = scrollState
                            ) {
                                itemsIndexed(
                                    items = getAllFoodCategories()
                                ) { _, category ->
                                    FoodCategoryChip(
                                        category = category.value,
                                        isSelected = selectedCategory == category,
                                        onSelectedCategoryChanged = {
                                            viewModel.onSelectedCategoryChanged(it)
                                            viewModel.onChangedCategoryScrollPosition(scrollState.firstVisibleItemIndex , scrollState.firstVisibleItemScrollOffset)
                                        },
                                        onExecuteSearch = { viewModel.newSearch() },

                                        )
                                }
                                scope.launch {
                                    scrollState.animateScrollToItem(index = viewModel.categoryScrollPosition , viewModel.categoryScrollPosition2)
                                }
                            }
                        }
                    }
                    LazyColumn {
                        itemsIndexed(
                            items = recipes
                        ) { index, item ->
                            RecipeCard(recipe = item, onClick = {})
                        }
                    }
                }

            }
        }

        return view
    }
}