package com.android.example.recipeapp.network.model

import com.android.example.recipeapp.domain.model.Recipe
import com.android.example.recipeapp.domain.util.DomainMapper
import com.android.example.recipeapp.util.DateUtils

class RecipeDtoMapper : DomainMapper<RecipeDto, Recipe> {
    override fun mapToDomainModel(entity: RecipeDto): Recipe {
        return Recipe(
            id = entity.pk,
            title = entity.title,
            publisher = entity.publisher,
            featuredImage = entity.featuredImage,
            rating = entity.rating,
            sourceUrl = entity.sourceUrl,
            ingredients = entity.ingredients ?: listOf(),
            dateAdded = DateUtils.longToDate(entity.longDateAdded),
            dateUpdated = DateUtils.longToDate(entity.longDateUpdated)
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            pk = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            longDateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            longDateUpdated = DateUtils.dateToLong(domainModel.dateUpdated)
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Recipe>): List<RecipeDto> {
        return initial.map {
            mapFromDomainModel(it)
        }
    }
}