package com.haikal.healthenics.api.data

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("foods")
    val foods: FoodList
)

data class FoodList(
    @SerializedName("food")
    val food: List<FoodItem>
)

/*data class NutritionResponse(
    @SerializedName("servings")
    val servings: ServingList
)*/




