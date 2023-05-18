package com.zgz.shoppingitems.shoppinglistsummary

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zgz.shoppingitems.database.ShoppingListDatabaseDao

class ShoppingListSummaryViewModelFactory(
            private val dataSource: ShoppingListDatabaseDao,
            private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShoppingListSummaryViewModel::class.java)) {
            return ShoppingListSummaryViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}