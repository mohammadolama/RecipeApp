package com.android.example.recipeapp.cache.model

import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.domain.util.DomainMapper
import com.android.example.recipeapp.network.model.RecipeDto
import com.android.example.recipeapp.util.DateUtils
import java.lang.StringBuilder

class RecipeEntityMapper : DomainMapper<RecipeEntity, Recipe> {


    override fun mapToDomainModel(entity: RecipeEntity): Recipe {
        return Recipe(
            id = entity.id,
            title = entity.title,
            publisher = entity.publisher,
            featuredImage = entity.featuredImage,
            rating = entity.rating,
            sourceUrl = entity.sourceUrl,
            ingredients = convertStringToList(entity.ingredients),
            dateAdded = DateUtils.longToDate(entity.dateAdded),
            dateUpdated = DateUtils.longToDate(entity.dateUpdated)
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeEntity {
        return RecipeEntity(
            id = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            ingredients = convertListToString(domainModel.ingredients),
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }

    private fun convertListToString(ingredients: List<String>): String {
        val ingredientsString = StringBuilder()
        for (ingredient in ingredients) {
            ingredientsString.append("$ingredient,")
        }
        return ingredientsString.toString()
    }

    private fun convertStringToList(string: String?): List<String> {
        val list: ArrayList<String> = ArrayList()

        string?.let {
            list.addAll(string.split(","))
        }
        return list
    }

    fun fromEntityList(initial: List<RecipeEntity>): List<Recipe>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<Recipe>): List<RecipeEntity>{
        return initial.map { mapFromDomainModel(it) }
    }

}