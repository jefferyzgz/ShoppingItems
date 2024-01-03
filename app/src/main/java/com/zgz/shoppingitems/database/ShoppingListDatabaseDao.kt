package com.zgz.shoppingitems.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.io.StringBufferInputStream

/**
 * Defines methods for using the ShoppingItem class with Room.
 */
@Dao
interface ShoppingListDatabaseDao {
    @Insert
    suspend fun insertItem(shoppingItem: ShoppingItem)

    @Insert
    suspend fun insertList(shoppingList: ShoppingList): Long

    @Update
    suspend fun updateItem(shoppingItem: ShoppingItem)

    @Update
    suspend fun updateList(shoppingList: ShoppingList)

    @Query("UPDATE shopping_list_table set list_name = :listName where list_id = :listId")
    suspend fun updateListName(listId: Long, listName: String)

    @Query("UPDATE shopping_item_table set item_state = :itemState where item_id = :itemId")
    suspend fun updateItemSatae(itemId: Long, itemState: Int)

    @Query("DELETE FROM shopping_item_table where item_id = :shoppingItemId")
    suspend fun deleteItem(shoppingItemId: Long) : Int

    @Query("DELETE FROM shopping_list_table where list_id = :listId")
    suspend fun deleteList(listId: Long) : Int

    /**
     * Select and return all items with state in a shopping list.
     */
    @Query("SELECT * from shopping_item_table where shopping_list_id = :listId and item_state = :itemState order by item_priority desc")
    fun getItemsByListIdAndState(listId: Long, itemState: Int): LiveData<List<ShoppingItem>>

    /**
     * Count the item number with state including in a shopping list.
     */
    @Query("SELECT count(item_id) from shopping_item_table where shopping_list_id = :listId and item_state = :itemState order by item_priority desc")
    suspend fun countItemsByListIdAndState(listId: Long, itemState: Int): Int

    /**
     * Select and return one item in a shopping list.
     */
    @Query("SELECT * from shopping_item_table where item_id = :itemId")
    suspend fun getItemById(itemId: Long): ShoppingItem


    /**
     * Select and return a shopping list.
     */
    @Query("SELECT * from shopping_list_table where list_id = :listId")
    fun getListByListId(listId: Long): LiveData<ShoppingList>

    /**
     * Select and return all undeleted shopping list.
     */
    @Query("SELECT * from shopping_list_table where state!=-1")
    fun getAllList(): LiveData<List<ShoppingList>>

    /**
     * Select and return all undeleted shopping list.
     */
//    @Query("SELECT * from shopping_list_table")
//    fun getAllListSummary(): LiveData<List<ShoppingList>>

    /**
     * Return the active shopping list.
     */
    @Query("SELECT * from shopping_list_table where active = 1")
    suspend fun getActiveList(): ShoppingList


}