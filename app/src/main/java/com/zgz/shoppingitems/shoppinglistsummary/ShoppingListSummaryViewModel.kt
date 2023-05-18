package com.zgz.shoppingitems.shoppinglistsummary

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zgz.shoppingitems.database.ShoppingList
import com.zgz.shoppingitems.database.ShoppingListDatabaseDao
import kotlinx.coroutines.launch

class ShoppingListSummaryViewModel(
        val database: ShoppingListDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    var shoppingListSummary = database.getAllList()

    private val _navigateToNewList = MutableLiveData<Long>()
    val navigateToNewList: LiveData<Long>
        get() = _navigateToNewList
    //val list = database.getAllListSummary()
    /**
     * Call this immediately after navigating to [SleepQualityFragment]
     *
     * It will clear the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    @SuppressLint("NullSafeMutableLiveData")
    fun doneNavigating() {
        _navigateToNewList.value = null
    }

    fun onNavigateToList(listId: Long) {
        _navigateToNewList.value = listId
    }

    /**
     * Executes when Create button is clicked.
     */
    fun onCreateNewList() {
        viewModelScope.launch {
            val newList = ShoppingList()
            var listId = insert(newList)
            _navigateToNewList.value = listId
        }
    }

    fun onDeleteList(listId: Long) {

        viewModelScope.launch {
            database.deleteList(listId)
        }
    }

    private suspend fun insert(list: ShoppingList): Long {

        var listId = database.insertList(list)
        return listId
    }

    init{
        initializeAllShoppingList()
    }

    private fun initializeAllShoppingList() {
        viewModelScope.launch {
            shoppingListSummary = database.getAllList()
         }
    }
}