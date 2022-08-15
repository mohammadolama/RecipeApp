package com.android.example.recipeapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
data class Recipe(
    var id: Int,
    var title: String,
    var publisher: String,
    var featuredImage: String,
    var rating: Int,
    var sourceUrl: String,
    var ingredients: List<String> = listOf(),
    var dateAdded: Date,
    var dateUpdated: Date,
    ) : Parcelable