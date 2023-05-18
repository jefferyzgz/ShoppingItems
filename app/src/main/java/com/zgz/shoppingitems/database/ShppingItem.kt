package com.zgz.shoppingitems.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_item_table")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var itemId: Long = 0L,

    @ColumnInfo(name = "item_name")
    var itemName: String = "",

    @ColumnInfo(name = "amount")
    var amount: Long = 0L,

    @ColumnInfo(name = "unit")
    var itemUnit: String = "",

    @ColumnInfo(name = "display_order")
    var displayOrder: Long = 0L,

    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0L,

    @ColumnInfo(name = "shopping_list_id", index = true)
    var shoppingListId: Long = 0L,

    /**
     * 0: the initial state
     * 1: bought state
     * 2: deleted state
     */
    @ColumnInfo(name = "item_state")
    var itemState: Int = 0,

    @ColumnInfo(name = "item_priority")
    var itemPriority: Int = 0
)