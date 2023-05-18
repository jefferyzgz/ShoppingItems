package com.zgz.shoppingitems.shoppinglist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zgz.shoppingitems.database.ShoppingListDatabaseDao

class ShoppingListViewModelFactory(
    private val listIdKey: Long,
    private val dataSource: ShoppingListDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            return ShoppingListViewModel(listIdKey, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}