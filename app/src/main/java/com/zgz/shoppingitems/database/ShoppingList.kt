package com.zgz.shoppingitems.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "shopping_list_table")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "list_id")
    var listId: Long = 0L,

    @ColumnInfo(name = "list_name")
    var listName: String = SimpleDateFormat("MM-dd-yyyy-HH:mm").format(Date(System.currentTimeMillis())),

    /**
     * State of the list.
     * 0: default
     * -1: deleted
     * 1: saved
     */
    @ColumnInfo(name = "state")
    var listState: Int = 0,

    /**
     * Is this list the active one.
     * 0: not active, default
     * 1: active, there is only one list is active list.
     */
    @ColumnInfo(name = "active")
    var active: Int = 0

)