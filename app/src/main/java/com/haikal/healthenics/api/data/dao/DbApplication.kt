package com.haikal.healthenics.api.data.dao

import android.app.Application
import androidx.room.Room

class DbApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "consumed_food").build()
    }
}