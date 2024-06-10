package com.haikal.healthenics.api


import com.haikal.healthenics.api.data.DetailFoodResponse
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.api.data.FoodResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface ApiService {
    @GET("server.api")
    suspend fun getSearchedFood(
        @Query("search_expression") query: String,
        @Query("page_number") pageNumber: Int,
        @Query("max_results") maxResults: Int,
        @Query("method") method: String = "foods.search",
        @Query("format") format: String = "json"

    ): FoodResponse

    @GET("server.api")
    fun getFoodDetails(
        @Query("food_id") foodId: String,
        @Query("method") method: String = "food.get.v4",
        @Query("format") format: String = "json"
    ): Call<DetailFoodResponse>
}