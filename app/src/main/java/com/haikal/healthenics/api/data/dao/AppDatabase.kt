package com.haikal.healthenics.api.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haikal.healthenics.api.data.FoodItem
import com.haikal.healthenics.utils.Converters

@Database(entities = [ConsumedFood::class, FoodItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}