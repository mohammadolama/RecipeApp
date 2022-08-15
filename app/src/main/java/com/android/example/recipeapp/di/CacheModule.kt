package com.android.example.recipeapp.di

import androidx.room.Room
import com.android.example.recipeapp.cache.RecipeDAO
import com.android.example.recipeapp.cache.database.AppDatabase
import com.android.example.recipeapp.cache.model.RecipeEntity
import com.android.example.recipeapp.cache.model.RecipeEntityMapper
import com.android.example.recipeapp.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDB(app: BaseApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_BASE)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRecipeDAO(app: AppDatabase): RecipeDAO {
        return app.recipeDao()
    }

    @Singleton
    @Provides
    fun provideCacheRecipeMapper(): RecipeEntityMapper {
        return RecipeEntityMapper()
    }

}