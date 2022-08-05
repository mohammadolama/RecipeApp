package com.android.example.recipeapp.di

import com.android.example.recipeapp.network.RecipeService
import com.android.example.recipeapp.network.model.RecipeDto
import com.android.example.recipeapp.network.model.RecipeDtoMapper
import com.android.example.recipeapp.repository.RecipeRepository
import com.android.example.recipeapp.repository.RecipeRepository_Impl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository {
        return RecipeRepository_Impl(recipeService, recipeDtoMapper)
    }


}