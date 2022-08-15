package com.android.example.recipeapp.interactors.recipe_list

import android.util.Log
import com.android.example.recipeapp.cache.RecipeDAO
import com.android.example.recipeapp.cache.model.RecipeEntityMapper
import com.android.example.recipeapp.domain.data.DataState
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.android.example.recipeapp.util.TAG
import kotlinx.coroutines.delay


class RestoreRecipes(
    private val recipeDao: RecipeDAO,
    private val entityMapper: RecipeEntityMapper,

    ) {
    fun excute(
        page: Int,
        query: String,

        ): Flow<DataState<List<Recipe>>> = flow {

        try {

            emit(DataState.loading())

            delay(1000)

            val cacheResult = if (query == "") {
                recipeDao.restoreAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)

            emit(DataState.success(data = list))


        } catch (e: Exception) {
            Log.d(TAG, "excute: ${e.message} ")
            emit(DataState.error<List<Recipe>>(e.message ?: "unknown error"))
        }

    }
}