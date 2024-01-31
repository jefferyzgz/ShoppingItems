package com.zgz.shoppingitems.shoppinglistsummary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zgz.shoppingitems.database.ShoppingItem
import com.zgz.shoppingitems.database.ShoppingList
import com.zgz.shoppingitems.database.ShoppingListDatabaseDao
import com.zgz.shoppingitems.databinding.ShoppingListSummaryItemBinding
//import com.zgz.shoppingitems.generated.callback.OnClickListener
import kotlin.coroutines.coroutineContext

class ShoppingListSummaryAdapter(val clickDeleteListener: ShoppingListSummaryDeleteListener,
                                 val clickSelectListener: ShoppingListSummarySelectListener)
    : ListAdapter<ShoppingList, ShoppingListSummaryAdapter.ViewHolder>(ShoppingListDiffCallBack()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, clickDeleteListener, clickSelectListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ShoppingListSummaryItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: ShoppingList, clickDeleteListener: ShoppingListSummaryDeleteListener,
                     clickSelectListener: ShoppingListSummarySelectListener) {
                val res = itemView.context.resources
                binding.itemName.text = item.listName
                //binding.itemAmount.text = item.listId.toString()
/*                binding.deleteButton.setOnClickListener{
                    //Toast.makeText(it.context, "dddddd:"+item.listId, Toast.LENGTH_SHORT).show()

                    //database.deleteList(item.listId)
                }*/
                binding.list = item
                binding.clickDeleteListener = clickDeleteListener
                binding.clickSelectListener = clickSelectListener
                binding.executePendingBindings()

            }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingListSummaryItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }
}

class ShoppingListDiffCallBack: DiffUtil.ItemCallback<ShoppingList>() {
    override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem.listId == newItem.listId
    }

    override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem == newItem
    }
}

class ShoppingListSummaryDeleteListener(val clickListener: (listId: Long) -> Unit) {
    fun onClick(list: ShoppingList) = clickListener(list.listId)
}

class ShoppingListSummarySelectListener(val clickListener: (listId: Long) -> Unit) {
    fun onClick(list: ShoppingList) = clickListener(list.listId)
}
