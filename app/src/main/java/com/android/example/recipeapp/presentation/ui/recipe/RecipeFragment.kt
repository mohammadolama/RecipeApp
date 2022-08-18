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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.example.recipeapp.presentation.BaseApplication
import com.android.example.recipeapp.presentation.components.shimmer.LoadingShimmerList
import com.android.example.recipeapp.presentation.components.RecipeView
import com.android.example.recipeapp.presentation.components.util.SnackbarController
import com.android.example.recipeapp.presentation.ui.theme.AppTheme
import com.android.example.recipeapp.presentation.ui.util.ConnectivityManager
import com.android.example.recipeapp.presentation.ui.util.DialogQueue
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()

    @Inject
    lateinit var application: BaseApplication

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    private var snackbarController = SnackbarController(lifecycleScope)

    private var recipeID: MutableState<Int> = mutableStateOf(-1)

    lateinit var dialogQueue: DialogQueue


    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeID")?.let { rID ->
            recipeID.value = rID
            viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(rID))
        }
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

                val loading = viewModel.loading.value
                val recipe = viewModel.recipe.value
                val scaffoldState = rememberScaffoldState()
                AppTheme(
                    darkTheme = application.isDark.value,
                    isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
                    loading = loading,
                    scaffoldState = scaffoldState,
                    dialogQueue = dialogQueue.queue.value
                ) {
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
                                    RecipeView(recipe = it)
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