package com.haikal.healthenics.api.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haikal.healthenics.api.data.DetailFoodResponse

@Entity(tableName = "consumed_food")
data class ConsumedFood(
    @PrimaryKey val foodId: Long,
    val foodName: String,
    val foodDescription: String,
    val foodType: String,
    val foodUrl: String,
    val calories: String,
    val protein: String,
    val fat: String,
    val carbohydrate: String
)