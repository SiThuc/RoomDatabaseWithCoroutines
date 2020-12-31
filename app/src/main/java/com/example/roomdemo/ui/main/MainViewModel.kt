package com.example.roomdemo.ui.main

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roomdemo.database.Product
import com.example.roomdemo.repository.ProductRepository

class MainViewModel : ViewModel() {
    private var repository: ProductRepository? = null
    private var allProducts: LiveData<List<Product>>? = null
    private var searchResults: MutableLiveData<List<Product>>? = null


    fun initReposototy(context: Context) {
        repository = ProductRepository(context)
        allProducts = repository!!.allProducts
        searchResults = repository!!.searchResults
    }

    fun insertProduct(product: Product) {
        repository!!.insertProduct(product)
    }

    fun findProduct(name: String) {
        repository!!.findProduct(name)
    }

    fun deleteProduct(name: String) {
        repository!!.deleteProduct(name)
    }

    fun getSearchResults(): MutableLiveData<List<Product>> {
        return searchResults!!
    }

    fun getAllProducts(): LiveData<List<Product>>? {
        return allProducts
    }

}