package com.example.shoplist.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoplist.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDatabaseDao {

    @Query("Select * From product")
    fun getAll(): Flow< List<Product> >

    @Query("Select * From product Where id = :id")
    fun getById(id: Int): Flow< Product >

    @Insert
    suspend fun add(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)
}