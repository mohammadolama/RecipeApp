package com.android.example.recipeapp.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.example.recipeapp.presentation.BaseApplication
import com.android.example.recipeapp.presentation.components.CircularIndeterminateProgressBar
import com.android.example.recipeapp.presentation.components.DefaultSnackbar
import com.android.example.recipeapp.presentation.components.LoadingShimmerList
import com.android.example.recipeapp.presentation.components.RecipeView
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.nimkat.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    @Inject
    lateinit var application: BaseApplication

    private var snackbarController = SnackbarController(lifecycleScope)

    private var recipeID: MutableState<Int> = mutableStateOf(-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeID")?.let { rID ->
            recipeID.value = rID
            viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(rID))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = ComposeView(requireContext())

        view.apply {
            setContent {

                val loading = viewModel.loading.value
                val recipe = viewModel.recipe.value
                val scaffoldState = rememberScaffoldState()
                AppTheme(darkTheme = application.isDark.value , loading , scaffoldState) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        snackbarHost = { scaffoldState.snackbarHostState }
                    ) { padding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            if (loading && recipe == null) {
                                Text(text = "Loading ...")
                                LoadingShimmerList(
                                    cardHeight = 250.dp,
                                    padding = 8.dp,
                                    forRecipeFragment = true,
                                    application
                                )
                            } else {
                                recipe?.let {
                                    if (it.id == 1) {
                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = "An error occurred with this recipe",
                                            actionLabel = "OK"
                                        )
                                    } else {
                                        RecipeView(recipe = it)
                                    }
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