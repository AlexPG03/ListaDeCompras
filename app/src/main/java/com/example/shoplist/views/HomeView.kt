@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shoplist.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.shoplist.MainActivity
import com.example.shoplist.models.Product
import com.example.shoplist.viewmodels.ProductViewModel
import java.nio.file.WatchEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: ProductViewModel){

    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Lista de Compra")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }


            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text("Añadir nuevo elemento") },
                    text = {
                        Column {
                            TextField(
                                modifier = Modifier
                                    .padding(bottom = 15.dp),
                                value = newItemName,
                                onValueChange = { newItemName = it },
                                label = { Text("Nombre del elemento") }
                            )

                            TextField(
                                value = newItemCategory,
                                onValueChange = { newItemCategory = it },
                                label = { Text("Categoria del elemento") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {

                            if (newItemName.isNotBlank()) {
                                val product = Product(
                                    name = newItemName,
                                    category = newItemCategory
                                )
                                viewModel.state.productList = viewModel.state.productList + product

                                viewModel.addProduct(product)
                                newItemName = ""
                                newItemCategory = ""
                                showAddDialog = false
                            }
                        }) {
                            Text("Añadir")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showAddDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    ){ pading ->
        HomeContent(pading, viewModel)
    }
}


@Composable
fun HomeContent(iterator: PaddingValues,viewModel: ProductViewModel){
    val state = viewModel.state
    var items by remember { mutableStateOf(listOf<Product>()) }
    items = state.productList
    var searchText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(iterator)
    ) {
        TextField(
            value = searchText,
            onValueChange = {searchText = it},
            label = {Text("Buscar")},
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Buscar"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            val filtredItems = items.filter {
                it.name.contains(searchText, ignoreCase = true) || it.category.contains(searchText, ignoreCase = true)
            }
            val scrollState = rememberLazyListState()
            LazyColumn (state = scrollState) {
                items(filtredItems) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = {checked ->
                                items = items.map { if (it == item){
                                    val aux = it.copy(isChecked = checked)
                                    viewModel.updateProduct(aux)
                                    aux
                                }else it }
                            }
                        )
                        Text(
                            text = item.name,
                            modifier = Modifier.weight(1f),
                            style = if (item.isChecked) MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough)
                            else MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = item.category,
                            modifier = Modifier.weight(1f),
                            style = if (item.isChecked) MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough)
                            else MaterialTheme.typography.bodySmall
                        )
                        IconButton(onClick =
                            {
                                viewModel.deleteProduct(item)
                                items = items - item
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}
