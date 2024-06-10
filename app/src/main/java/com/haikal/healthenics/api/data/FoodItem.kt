package com.haikal.healthenics.api.data

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.haikal.healthenics.utils.Converters
import kotlinx.parcelize.Parcelize

@Entity
data class FoodItem(
    @SerializedName("food_id")
    @PrimaryKey val foodId: Long,
    @SerializedName("food_name")
    val foodName: String,
    @SerializedName("food_description")
    val foodDescription: String,
    @SerializedName("food_type")
    val foodType: String,
    @SerializedName("food_url")
    val foodUrl: String
)