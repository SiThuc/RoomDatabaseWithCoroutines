package com.example.roomdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.R
import com.example.roomdemo.adapter.ProductAdapter
import com.example.roomdemo.database.Product
import java.util.*

class MainFragment : Fragment() {

    private var adapter: ProductAdapter? = null

    private var addButton: Button?=null
    private var findButton: Button?=null
    private var deleteButton: Button?=null
    private var productID: TextView?= null
    private var productName: EditText?=null
    private var productQuantity: EditText?=null


    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.initReposototy(requireContext())

        val root = inflater.inflate(R.layout.main_fragment, container, false)
        addButton = root.findViewById(R.id.btn_add) as Button
        findButton = root.findViewById(R.id.btn_find) as Button
        deleteButton = root.findViewById(R.id.btn_delete) as Button

        productID = root.findViewById(R.id.txt_product_id) as TextView
        productName = root.findViewById(R.id.edt_product_name) as EditText
        productQuantity = root.findViewById(R.id.edt_product_quantity) as EditText

        recyclerSetup(root)
        listenerSetup()
        observerSetup()


        return root
    }


    private fun clearFields() {
        productID!!.text = ""
        productName!!.setText("")
        productQuantity!!.setText("")
    }


    private fun listenerSetup() {
        addButton!!.setOnClickListener {
            val name = productName!!.text.toString()
            val quantity = productQuantity!!.text.toString()
            if (name != "" && quantity != "") {
                val product = Product(name, Integer.parseInt(quantity))
                viewModel.insertProduct(product)
                clearFields()
            } else {
                productID!!.text = "Incomplete information"
            }
        }

        findButton!!.setOnClickListener { viewModel.findProduct(productName!!.text.toString()) }

        deleteButton!!.setOnClickListener {
            viewModel.deleteProduct(productName!!.text.toString())
            clearFields()
        }
    }

    private fun observerSetup() {
        viewModel.getAllProducts()?.observe(viewLifecycleOwner, Observer { products ->
            products?.let {
                adapter?.setProductList(it)
            }
        })

        viewModel.getSearchResults().observe(viewLifecycleOwner, Observer { products ->
            products?.let {
                if (it.isNotEmpty()) {
                    productID!!.text = String.format(Locale.US, "%d", it[0].id)
                    productName!!.setText(it[0].productName)
                    productQuantity!!.setText(String.format(Locale.US, "%d",
                        it[0].quantity))
                } else {
                    productID!!.text = "No Match"
                }
            }
        })
    }

    private fun recyclerSetup(root: View) {
        adapter = ProductAdapter(R.layout.product_item)
        val recyclerView: RecyclerView? = root.findViewById(R.id.recycler_product)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
    }
}