package com.android.example.recipeapp.network.responses

import com.android.example.recipeapp.network.model.RecipeDto
import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse (
    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var recipes: List<RecipeDto>,

    ){
    override fun toString(): String {
        return "RecipeSearchResponse(count=$count, recipes=$recipes)"
    }
}