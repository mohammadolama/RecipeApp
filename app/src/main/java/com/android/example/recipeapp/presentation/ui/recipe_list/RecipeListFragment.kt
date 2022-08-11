package com.android.example.recipeapp.presentation.ui.recipe_list

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.example.recipeapp.R
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
                                        viewModel.newSearch()
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
                                if (loading) {
                                    LoadingShimmerList(cardHeight = 250.dp, padding = 8.dp)
                                } else {
                                    LazyColumn {
                                        itemsIndexed(
                                            items = recipes
                                        ) { _, item ->
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
