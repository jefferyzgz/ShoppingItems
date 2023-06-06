package com.zgz.shoppingitems.runninglist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.zgz.shoppingitems.database.ShoppingItem
import com.zgz.shoppingitems.database.ShoppingList
import com.zgz.shoppingitems.database.ShoppingListDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RunningShoppingListViewModel(
    private val listIdKey: Long = 0L,
    val database: ShoppingListDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    var currentShoppingList = database.getListByListId(listIdKey)

    var currentShoppingItems = database.getItemsByListIdAndState(listIdKey, 0)
    var boughtShoppingItems = database.getItemsByListIdAndState(listIdKey, 1)
    var deletedShoppingItems = database.getItemsByListIdAndState(listIdKey, 2)

    val currentShoppingListName = currentShoppingList.value?.listName

    var fragmentList = listOf(
        BoughtListFragment(),
        CurrentListFragment(),
        DeletedListFragment()
    )

    private suspend fun insertShoppingList(list: ShoppingList): Long {
        var listCreated: Long
        withContext(Dispatchers.IO) {
            listCreated = database.insertList(list)
            Log.i("createdNewList", listCreated.toString())
            //return@withContext listCreated
        }
        return listCreated
    }

    init {
        getShoppingList()
    }

    private fun getShoppingList() {
        viewModelScope.launch {
            currentShoppingList = database.getListByListId(listIdKey)
            currentShoppingItems = database.getItemsByListIdAndState(listIdKey, 0)
            boughtShoppingItems = database.getItemsByListIdAndState(listIdKey, 1)
            deletedShoppingItems = database.getItemsByListIdAndState(listIdKey, 2)
            /*fragmentList = listOf(
                BoughtListFragment(listIdKey),
                CurrentListFragment(),
                DeletedListFragment(listIdKey),
            )*/
            Log.i(
                "vm",
                "list:" + currentShoppingList.value.toString() + ",items:" + currentShoppingItems.value.toString()
            )
        }
    }

    private fun createNewShoppingList() {
        viewModelScope.launch {
            val list = ShoppingList()
            var createdListId = insertShoppingList(list)
            Log.i("ini-createdNewList", createdListId.toString())
            var activeList = database.getListByListId(createdListId)
            //currentShoppingList.value = activeList
            Transformations.map(currentShoppingList) { nights ->
                Log.i("createdNewList-vm-1", "111:" + nights.toString())
            }
            Log.i("get-createdNewList", activeList.toString())
            //activeShoppingItems.value = database.getItemsByListId(listIdKey)
        }
    }

    fun insertNewShoppingItemToList(
        listId: Long,
        itemName: String,
        amount: Long,
        unit: String,
        priority: Int
    ) {
        viewModelScope.launch {
            var item = ShoppingItem()
            item.shoppingListId = listId
            item.itemName = itemName
            item.amount = amount
            item.itemUnit = unit
            item.itemPriority = priority
            database.insertItem(item)
        }
    }

    fun updateShoppingListName(listId: Long, listName: String) {
        viewModelScope.launch {
            val unit = database.updateListName(listId, listName)

        }
    }

    fun deleteShoppingItem(itemId: Long) {
        viewModelScope.launch {
            database.deleteItem(itemId)
        }
    }

    /**
     * States of ShoppingItem
     * 0: the initial state
     * 1: bought state
     * 2: deleted state
     */
    fun markShoppingItemAsBought(itemId: Long) {
        viewModelScope.launch {
            database.updateItemSatae(itemId, 1)
        }
    }

    fun markShoppingItemAsDelete(itemId: Long) {
        viewModelScope.launch {
            database.updateItemSatae(itemId, 2)
        }
    }

    fun markShoppingItemAsCurrent(itemId: Long) {
        viewModelScope.launch {
            database.updateItemSatae(itemId, 0)
        }
    }

    fun deleteShoppingItemAsCurrent(itemId: Long) {
        viewModelScope.launch {
            database.deleteItem(itemId)
        }
    }

    fun getCurrentShoppingItems(listId: Long) {
        viewModelScope.launch {
            database.getItemsByListIdAndState(listId, 0)
        }
    }

    fun getBoughtShoppingItems(listId: Long) {
        viewModelScope.launch {
            database.getItemsByListIdAndState(listId, 1)
        }
    }

    fun getDeletedShoppingItems(listId: Long) {
        viewModelScope.launch {
            database.getItemsByListIdAndState(listId, 2)
        }
    }
}