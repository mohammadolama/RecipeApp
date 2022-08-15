package com.android.example.recipeapp.di

import com.android.example.recipeapp.cache.RecipeDAO
import com.android.example.recipeapp.cache.model.RecipeEntityMapper
import com.android.example.recipeapp.interactors.recipe.GetRecipe
import com.android.example.recipeapp.interactors.recipe_list.RestoreRecipes
import com.android.example.recipeapp.interactors.recipe_list.SearchRecipes
import com.android.example.recipeapp.network.RecipeService
import com.android.example.recipeapp.network.model.RecipeDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {


    @ViewModelScoped
    @Provides
    fun provideSearchRecipes(
        recipeService: RecipeService,
        recipeDAO: RecipeDAO,
        recipeEntityMapper: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper,
    ): SearchRecipes {
        return SearchRecipes(
            recipeDao = recipeDAO,
            recipeService = recipeService,
            entityMapper = recipeEntityMapper,
            dtoMapper = recipeDtoMapper
        )

    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDAO: RecipeDAO,
        recipeEntityMapper: RecipeEntityMapper,
    ): RestoreRecipes {
        return RestoreRecipes(
            recipeDao = recipeDAO,
            entityMapper = recipeEntityMapper,
        )

    }


    @ViewModelScoped
    @Provides
    fun provideGetRecipe(
        recipeService: RecipeService,
        recipeDAO: RecipeDAO,
        recipeEntityMapper: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper,
    ): GetRecipe {
        return GetRecipe(
            recipeDao = recipeDAO,
            recipeService = recipeService,
            recipeEntityMapper,
            recipeDtoMapper
        )

    }
}