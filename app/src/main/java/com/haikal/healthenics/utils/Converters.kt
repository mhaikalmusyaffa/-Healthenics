package com.haikal.healthenics.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haikal.healthenics.api.data.DetailFoodResponse

class Converters {

    @TypeConverter
    fun fromServing(serving: DetailFoodResponse.Food.Servings.Serving): String {
        return Gson().toJson(serving)
    }

    @TypeConverter
    fun toServing(servingString: String): DetailFoodResponse.Food.Servings.Serving {
        val type = object : TypeToken<DetailFoodResponse.Food.Servings.Serving>() {}.type
        return Gson().fromJson(servingString, type)
    }
}