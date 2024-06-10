package com.haikal.healthenics.api.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.dao.ConsumedFood

@Dao
interface FoodDao {
    @Insert
    suspend fun insertFoodItem(foodItem: FoodItem)

    @Insert
    suspend fun insertConsumedFood(consumedFood: ConsumedFood)

    @Query("SELECT * FROM consumed_food")
    fun getAllConsumedFoods(): LiveData<List<ConsumedFood>>

    @Query("DELETE FROM consumed_food WHERE foodId = :foodId")
    fun deleteFoodById(foodId: Int)
}