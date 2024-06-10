package com.haikal.healthenics.api.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haikal.healthenics.api.data.DetailFoodResponse
import java.io.Serializable

@Entity(tableName = "food_entity")
data class FoodEntity(
    @PrimaryKey val foodId: Long,
    val foodName: String,
    val foodDescription: String,
    val foodType: String,
    val foodUrl: String,
    val calories: String,
    val protein: String,
    val fat: String,
    val carbohydrate: String


): Serializable