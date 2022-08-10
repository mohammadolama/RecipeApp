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
import com.android.example.recipeapp.R
import com.android.example.recipeapp.presentation.BaseApplication
import com.android.example.recipeapp.presentation.components.*
import com.nimkat.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    val viewModel: RecipeListViewModel by viewModels()


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


                    Scaffold(
                        topBar = {
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
                                onChangedCategoryScrollPosition = viewModel::onChangedCategoryScrollPosition,
                                onToggleTheme = {
                                    application.toggleLightTheme()
                                }
                            )
                        },
                        bottomBar = {
                            MyBottomBar()
                        },
                        drawerContent = {
                            Drawer()
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
                            }

                        }
                    }
                }

            }
        }

        return view
    }
}


@Composable
fun MyBottomBar() {
    BottomNavigation(
        elevation = 12.dp
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.BrokenImage, "a") }, selected = false, onClick = {}
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Search, "a") }, selected = true, onClick = {}
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, "a") }, selected = false, onClick = {}
        )
    }
}




@Composable
fun Drawer(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
    ) {

            Image(
                painterResource(R.drawable.ic_profile),
                null,
                Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 24.dp, 0.dp, 0.dp)
            )

            Text(
                stringResource(R.string.drawer_title),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 12.dp, 0.dp, 0.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            val texts = listOf(
                R.string.drawer_desc_1,
                R.string.drawer_desc_2,
                R.string.drawer_desc_3,
                R.string.drawer_desc_4
            )

            for (i in 0 until 4) {
                Row(modifier = Modifier.padding(16.dp, 4.dp, 16.dp, 4.dp)) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        null,
                        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        stringResource(texts[i]),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }

                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row {
                        Text(
                            text = stringResource(R.string.free_sign_in_login),
                            fontSize = 20.sp,
                        )
                    }
                }

            Spacer(modifier = Modifier.weight(1F))


        Row(
            modifier = Modifier.padding(20.dp, 5.dp, 20.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.background_color),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(0.1F))
            Image(
                painterResource(R.drawable.ic_day),
                null,
                Modifier.size(20.dp)
            )
            Switch(
                checked = false,
                onCheckedChange = {

                },
                enabled = true,
                modifier = Modifier.padding(6.dp, 0.dp, 6.dp, 0.dp)
            )
            Image(
                painterResource(R.drawable.ic_night),
                null,
                Modifier.size(20.dp)
            )
        }


    }
}
