package com.example.shoplist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shoplist.models.Product

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDatabaseDao
}