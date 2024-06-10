package com.haikal.healthenics.api.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailFoodResponse(
    @SerializedName("food")
    val food: Food
) : Parcelable {
    @Parcelize
    data class Food(
        @SerializedName("food_id")
        val foodId: String,
        @SerializedName("food_name")
        val foodName: String,
        @SerializedName("food_type")
        val foodType: String,
        @SerializedName("food_url")
        val foodUrl: String,
        @SerializedName("servings")
        val servings: Servings
    ) : Parcelable {
        @Parcelize
        data class Servings(
            @SerializedName("serving")
            val serving: List<Serving>
        ) : Parcelable {
            @Parcelize
            data class Serving(
                @SerializedName("calcium")
                val calcium: String,
                @SerializedName("calories")
                val calories: String,
                @SerializedName("carbohydrate")
                val carbohydrate: String,
                @SerializedName("cholesterol")
                val cholesterol: String,
                @SerializedName("fat")
                val fat: String,
                @SerializedName("fiber")
                val fiber: String,
                @SerializedName("iron")
                val iron: String,
                @SerializedName("measurement_description")
                val measurementDescription: String,
                @SerializedName("metric_serving_amount")
                val metricServingAmount: String,
                @SerializedName("metric_serving_unit")
                val metricServingUnit: String,
                @SerializedName("monounsaturated_fat")
                val monounsaturatedFat: String,
                @SerializedName("number_of_units")
                val numberOfUnits: String,
                @SerializedName("polyunsaturated_fat")
                val polyunsaturatedFat: String,
                @SerializedName("potassium")
                val potassium: String,
                @SerializedName("protein")
                val protein: String,
                @SerializedName("saturated_fat")
                val saturatedFat: String,
                @SerializedName("serving_description")
                val servingDescription: String,
                @SerializedName("serving_id")
                val servingId: String,
                @SerializedName("serving_url")
                val servingUrl: String,
                @SerializedName("sodium")
                val sodium: String,
                @SerializedName("sugar")
                val sugar: String,
                @SerializedName("vitamin_a")
                val vitaminA: String,
                @SerializedName("vitamin_c")
                val vitaminC: String
            ) : Parcelable
        }
    }
}