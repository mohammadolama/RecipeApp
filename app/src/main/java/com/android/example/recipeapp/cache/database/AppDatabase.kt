package com.android.example.recipeapp.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.example.recipeapp.cache.RecipeDAO
import com.android.example.recipeapp.cache.model.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDAO

    companion object{
        val DATABASE_BASE = "recipe_db"
    }




}