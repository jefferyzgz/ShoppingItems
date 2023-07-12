package com.zgz.shoppingitems.shoppinglist

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zgz.shoppingitems.database.ShoppingItem
import com.zgz.shoppingitems.database.ShoppingList
import com.zgz.shoppingitems.databinding.ShoppingListItemBinding


class ShoppingItemAdapter(val longClickModifyListener: ShoppingItemLongClickListener):
    ListAdapter<ShoppingItem, ShoppingItemAdapter.ViewHolder>(ShoppingItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, longClickModifyListener)
    }

    fun fetchItemId(position: Int): Long {
        val item = getItem(position)
        val itemId = item.itemId
        Log.i("Swipe", "delete itemId="+itemId)
        return itemId
    }

    class ViewHolder private constructor(val binding: ShoppingListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItem, longClickModifyListener: ShoppingItemLongClickListener) {
            //val res = itemView.context.resources
            binding.itemAmount.text = item.amount.toString()
            var privority = "*"
            when(item.itemPriority) {
                0 -> privority = "*"
                1 -> privority = "**"
                2 -> privority = "***"
            }
            binding.itemName.text = privority + " " + item.itemName
            binding.itemUnit.text = item.itemUnit
            binding.item = item
            binding.longClickModifyListener = longClickModifyListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}

class ShoppingItemDiffCallBack: DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}

class ShoppingItemSwipeToDeleteCallBack(private val viewModel: ShoppingListViewModel,
                                        private val lifecycleOwner: LifecycleOwner,
                                        private val adapter: ShoppingItemAdapter) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    //private Drawable  icon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_delete);
    private var background = ColorDrawable(Color.parseColor("#3df5ae"))
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private var textOfAction = TextPaint()
    private val textHeight = 50.0f
    init {
        textOfAction.isAntiAlias = true
        textOfAction.color = Color.parseColor("#f5eeed")
        textOfAction.textSize = textHeight
    }
    private var text: String = "DELETE"

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView: View = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val backgroundCornerOffset = 20
        if (dX > 0) { // Swiping to the right
            background = ColorDrawable(Color.parseColor("#f56156"))
            background.setBounds(
                itemView.getLeft(), itemView.getTop(),
                itemView.getLeft() + dX.toInt() + backgroundCornerOffset,
                itemView.getBottom()
            )
        } else if (dX < 0) { // Swiping to the left
            background = ColorDrawable(Color.parseColor("#355096"))
            background.setBounds(
                itemView.getRight() + dX.toInt() - backgroundCornerOffset,
                itemView.getTop(), itemView.getRight(), itemView.getBottom()
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }
        background.draw(c)

        val textWidth = textOfAction.measureText(text)
        var textX = itemView.right - itemView.paddingRight - textWidth
        val textY = itemView.top + itemHeight / 2f + textHeight / 2f
        if (dX > 0) { // Swiping to the right
            textX = 10f

            text = "DELETE"
        } else if (dX < 0)  {
            text = "ARCHIVE"
        } else {
            text = ""
        }
        c?.drawText(text, textX, textY, textOfAction)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //
        Log.i("Swipe", "onMove!")
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val position = viewHolder.adapterPosition
        val itemId = adapter.fetchItemId(position)

        viewModel.markShoppingItemAsDelete(itemId)
        //Log.i("Swipe", "position="+position + "++id=" + itemId)
    }

}

/*class ShoppingItemSwipeDeleteListener(val swipeListener: (itemId: Long) -> Unit) {
    fun onClick(item: ShoppingItem) = swipeListener(item.itemId)
}*/
class ShoppingItemLongClickListener(val longClickListener: (itemId: ShoppingItem) -> Unit) {
    fun onLongClick(item: ShoppingItem) : Boolean {
        longClickListener(item)
        return true
    }
    //fun onClick(item: ShoppingItem) = longClickListener(item.itemId)
}
/*
class ShoppingListSummarySelectListener(val clickListener: (listId: Long) -> Unit) {
    fun onClick(list: ShoppingList) = clickListener(list.listId)
}
*/