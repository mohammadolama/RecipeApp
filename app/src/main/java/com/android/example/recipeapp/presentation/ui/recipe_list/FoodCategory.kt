package com.android.example.recipeapp.presentation.ui.recipe_list

enum class FoodCategory(val value: String) {
    ERROR("Error"),
    CHICKEN("Chicken"),
    BEEF("Beef"),
    SOUP("Soup"),
    DESSERT("Dessert"),
    VEGETARIAN("Vegetarian"),
    MILK("Milk"),
    VEGAN("Vegan"),
    PIZZA("Pizza"),
    DONUT("Donut")
}

fun getAllFoodCategories(): List<FoodCategory> {
    return FoodCategory.values().toList()
}

fun getFoodCategory(value: String): FoodCategory? {
    val map = FoodCategory.values().associateBy(FoodCategory::value)

    return map[value]
}