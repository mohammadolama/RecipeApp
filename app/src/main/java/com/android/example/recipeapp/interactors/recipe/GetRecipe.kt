package com.android.example.recipeapp.interactors.recipe

import android.util.Log
import com.android.example.recipeapp.cache.RecipeDAO
import com.android.example.recipeapp.cache.model.RecipeEntityMapper
import com.android.example.recipeapp.domain.data.DataState
import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.network.RecipeService
import com.android.example.recipeapp.network.model.RecipeDtoMapper
import com.android.example.recipeapp.util.MY_TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeDao: RecipeDAO,
    private val recipeService: RecipeService,
    private val recipeEntityMapper: RecipeEntityMapper,
    private val recipeDtoMapper: RecipeDtoMapper
) {

    fun excute(
        recipeInt: Int,
        token: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Recipe>> = flow {
        try {

            emit(DataState.loading())

            delay(1000)

            var recipe = getRecipeFromCache(recipeId = recipeInt)

            if (recipe != null) {
                emit(DataState.success(recipe))
            } else {

                if (isNetworkAvailable) {
                    val networkRecipe = getRecipeFromNetwork(token, recipeInt)

                    recipeDao.insertRecipe(recipeEntityMapper.mapFromDomainModel(networkRecipe))
                }

                recipe = getRecipeFromCache(recipeId = recipeInt)
                if (recipe != null) {
                    emit(DataState.success(recipe))
                } else {
                    throw Exception("dada,dnadnadnajk")
                }
            }


        } catch (e: Exception) {
            Log.d(MY_TAG, "excute: ${e.message} ")
            emit(DataState.error<Recipe>(e.message ?: "unknown error"))
        }
    }


    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            recipeEntityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return recipeDtoMapper.mapToDomainModel(recipeService.get(token = token, recipeId))
    }
}