package com.example.roomdemo.repository

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roomdemo.database.ProductDAO
import com.example.roomdemo.database.ProductRoomDatabase
import com.example.roomdemo.database.Product
import kotlinx.coroutines.*

class ProductRepository(context: Context) {
    val searchResults = MutableLiveData<List<Product>>()
    private var productDao: ProductDAO?

    val allProducts: LiveData<List<Product>>?

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        val db: ProductRoomDatabase? = ProductRoomDatabase.getDatabase(context)
        productDao = db?.productDao()
        allProducts = productDao?.getAllProducts()
    }

//    fun asyncFinished(results: List<Product>) {
//        searchResults.value = results
//    }


    fun insertProduct(newProduct: Product) {
        coroutineScope.launch {
            insertByCoroutines(newProduct)
        }
    }

    fun deleteProduct(name: String) {
        coroutineScope.launch {
            deleteByCoroutines(name)
        }
    }

    fun findProduct(name: String){
        //val coroutinesScope2 = CoroutineScope(Dispatchers.Main)

        coroutineScope.launch(Dispatchers.Main){
            Log.i("TAG", "Task 1 ${Thread.currentThread().name}")
            searchResults.value =  coroutineScope.async {
                findByCoroutines(name)
            }.await()
        }
    }


//    fun deleteProduct(name: String) {
//        val task = DeleteAsyncTask(productDao)
//        task.execute(name)
//    }

//    fun findProduct(name: String) {
//        val task = QueryAsyncTask(productDao)
//        task.delegate = this
//        task.execute(name)
//    }

//    fun insertProduct(newProduct: Product) {
//        val task = InsertAsyncTask(productDao)
//        task.execute(newProduct)
//    }


///*******************By Using Coroutines*********************************
    suspend fun deleteByCoroutines(name: String) {
        productDao?.deleteProduct(name)
    }

    suspend fun insertByCoroutines(product: Product) {
        productDao?.insertProduct(product)
    }

    suspend fun findByCoroutines(name: String): List<Product>? {
        Log.i("TAG", "Task 2 ${Thread.currentThread().name}")
        return productDao?.findProduct(name)
    }



    //******************** Using SyncTask*******************************

//    private class QueryAsyncTask constructor(val asyncTaskDao: ProductDAO?) :
//            AsyncTask<String, Void, List<Product>>() {
//
//        var delegate: ProductRepository? = null
//
//        override fun doInBackground(vararg params: String?): List<Product> {
//            return asyncTaskDao?.findProduct(params[0]!!)!!
//        }
//
//        override fun onPostExecute(result: List<Product>?) {
//            delegate?.asyncFinished(result!!)
//        }
//    }

//    private class InsertAsyncTask constructor(val asyncTaskDao: ProductDAO?) :
//            AsyncTask<Product, Void, Void>() {
//        override fun doInBackground(vararg params: Product?): Void? {
//            asyncTaskDao?.insertProduct(params[0]!!)
//            return null
//        }
//    }

//    private class DeleteAsyncTask constructor(val asyncTaskDao: ProductDAO?) :
//        AsyncTask<String, Void, Void>() {
//        override fun doInBackground(vararg params: String?): Void? {
//            asyncTaskDao?.deleteProduct(params[0]!!)
//            return null
//        }
//    }

}