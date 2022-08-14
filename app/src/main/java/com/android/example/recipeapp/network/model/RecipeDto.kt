package com.android.example.recipeapp.network.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(

    @SerializedName("pk")
    var pk: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("publisher")
    var publisher: String,

    @SerializedName("featured_image")
    var featuredImage: String,

    @SerializedName("rating")
    var rating: Int,

    @SerializedName("source_url")
    var sourceUrl: String,

    @SerializedName("ingredients")
    var ingredients: List<String> = listOf(),

    @SerializedName("long_date_added")
    var longDateAdded: Long,

    @SerializedName("long_date_updated")
    var longDateUpdated: Long,
) {
    override fun toString(): String {
        return "RecipeNetworkEntity(pk=$pk, title=$title, publisher=$publisher, rating=$rating)"
    }
}