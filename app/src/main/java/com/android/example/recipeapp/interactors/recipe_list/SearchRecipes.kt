package com.android.example.recipeapp.interactors.recipe_list

import com.android.example.recipeapp.cache.RecipeDAO
import com.android.example.recipeapp.cache.model.RecipeEntityMapper
import com.android.example.recipeapp.domain.data.DataState
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.network.RecipeService
import com.android.example.recipeapp.network.model.RecipeDtoMapper
import com.android.example.recipeapp.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.Exception

class SearchRecipes(
    private val recipeDao: RecipeDAO,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
) {
    fun excute(
        token: String,
        page: Int,
        query: String

    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading<List<Recipe>>())

            delay(1000)

            if (query == "Error") {
                throw Exception("Search Failed!")
            }


            val recipes = dtoMapper.toDomainList(
                recipeService.search(token = token, page = page, query = query).recipes
            )

            recipeDao.insertRecipes(
                entityMapper.toEntityList(recipes)
            )

            val cacheResult = if (query == "") {
                recipeDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)

            emit(DataState.success(data = list))

        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(message = e.message ?: "unknown error"))
        }
    }
}