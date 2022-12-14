package com.android.example.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.android.example.recipeapp.datastore.SettingsDatastore
import com.android.example.recipeapp.presentation.BaseApplication
import com.android.example.recipeapp.presentation.components.RecipeList
import com.android.example.recipeapp.presentation.components.SearchAppBar
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.android.example.recipeapp.presentation.ui.theme.AppTheme
import com.android.example.recipeapp.presentation.ui.util.ConnectivityManager
import com.android.example.recipeapp.presentation.ui.util.DialogQueue
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var datastore: SettingsDatastore

    private val viewModel: RecipeListViewModel by viewModels()

    private var snackbarController = SnackbarController(lifecycleScope)

    lateinit var dialogQueue: DialogQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogQueue = viewModel.dialogQueue
    }

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

                val page = viewModel.page.value

                val scaffoldState = rememberScaffoldState()

                AppTheme(
                    darkTheme = datastore.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    loading = loading,
                    scaffoldState = scaffoldState,
                    dialogQueue = dialogQueue.queue.value
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
                                    datastore.toggleTheme()
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
                                navController = findNavController(),
                                isDark = datastore.isDark.value
                            )

                        }
                    }
                }

            }
        }

        return view
    }
}
