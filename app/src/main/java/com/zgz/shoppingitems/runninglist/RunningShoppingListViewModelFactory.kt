package com.zgz.shoppingitems.runninglist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zgz.shoppingitems.database.ShoppingListDatabaseDao

class RunningShoppingListViewModelFactory (
    private val listIdKey: Long,
    private val dataSource: ShoppingListDatabaseDao,
    private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(RunningShoppingListViewModel::class.java)) {
                return RunningShoppingListViewModel(listIdKey, dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}