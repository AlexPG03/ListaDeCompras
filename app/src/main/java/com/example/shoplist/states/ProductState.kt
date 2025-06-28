package com.example.shoplist.states

import com.example.shoplist.models.Product

data class ProductState(
    var productList: List<Product> = emptyList()
)
