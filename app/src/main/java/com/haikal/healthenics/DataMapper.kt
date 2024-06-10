package com.haikal.healthenics.utils

import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.model.FoodEntity

object DataMapper {
    fun mapResponseToEntity(response: FoodItem): FoodEntity {
        return FoodEntity(
            response.foodId,
            response.foodName,
            response.foodDescription,
            response.foodType,
            response.foodUrl,
            calories = "asus",
            protein = "s",
            fat = "s",
            carbohydrate = "s"
        )
    }

    fun mapResponsesToEntities(responses: List<FoodItem>): List<FoodEntity> {
        return responses.map {
            mapResponseToEntity(it)
        }
    }
}