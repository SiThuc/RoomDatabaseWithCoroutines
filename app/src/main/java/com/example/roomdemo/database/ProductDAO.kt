package com.example.roomdemo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("select * from products where productName = :name")
    fun findProduct(name: String): List<Product>

    @Query("delete from products where productName = :name")
    fun deleteProduct(name: String)

    @Query("select * from products")
    fun getAllProducts(): LiveData<List<Product>>

}