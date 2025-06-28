package com.example.shoplist.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplist.models.Product
import com.example.shoplist.room.ProductDatabaseDao
import com.example.shoplist.states.ProductState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductViewModel(
    private val dao: ProductDatabaseDao
): ViewModel() {
    var state by mutableStateOf(ProductState())
        private set

    init {
        viewModelScope.launch{
            dao.getAll().collectLatest {
                state = state.copy(
                    productList = it
                )
            }
        }
    }

    fun addProduct(product: Product) = viewModelScope.launch {
        dao.add(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch {
        dao.update(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        dao.delete(product)
    }

}