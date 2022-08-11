package com.android.example.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.example.recipeapp.presentation.BaseApplication
import com.android.example.recipeapp.presentation.components.*
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.nimkat.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    val viewModel: RecipeListViewModel by viewModels()

    private var snackbarController = SnackbarController(lifecycleScope)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = ComposeView(requireContext())

        view.apply {
            setContent {
                AppTheme(darkTheme = application.isDark.value) {
                    val recipes = viewModel.recipes.value

                    val query = viewModel.query.value

                    val focusManager = LocalFocusManager.current

                    val selectedCategory = viewModel.selectedCategory.value

                    val scope = rememberCoroutineScope()

                    val loading = viewModel.loading.value

                    val page = viewModel.page.value

                    val scaffoldState = rememberScaffoldState()

                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                newSearch = {
                                    if (viewModel.selectedCategory.value?.value == "Milk") {

                                        snackbarController.getScope().launch {
                                            snackbarController.showSnackbar(
                                                scaffoldState,
                                                message = "Invalid Category MILK!",
                                                actionLabel = "Hide",
                                            )
                                        }
                                    } else {
                                        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
                                    }


                                },

                                focusManager = focusManager,
                                scrollPosition = viewModel.categoryScrollPosition,
                                scrollOffset = viewModel.categoryScrollPosition2,
                                scope = scope,
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                onChangedCategoryScrollPosition = viewModel::onChangedCategoryScrollPosition,
                                onToggleTheme = {
                                    application.toggleLightTheme()
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
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = MaterialTheme.colors.background)
                            ) {
                                if (loading && recipes.isEmpty()) {
                                    LoadingShimmerList(cardHeight = 250.dp, padding = 8.dp)
                                } else {
                                    LazyColumn {
                                        itemsIndexed(
                                            items = recipes
                                        ) { index, item ->
                                            viewModel.onChangeRecipeScrollPosition(index)
                                            if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                                                viewModel.onTriggerEvent(RecipeListEvent.NextPageEvent)
                                            }
                                            RecipeCard(recipe = item, onClick = {})
                                        }
                                    }
                                }
                                CircularIndeterminateProgressBar(isDisplayed = loading)
                                DefaultSnackbar(
                                    snackbarHostState = scaffoldState.snackbarHostState,
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                ) {
                                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                }
                            }

                        }
                    }
                }

            }
        }

        return view
    }
}
