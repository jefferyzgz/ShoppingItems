package com.zgz.shoppingitems.runninglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.zgz.shoppingitems.R
import com.zgz.shoppingitems.database.ShoppingListDatabase
import com.zgz.shoppingitems.databinding.FragmentCurrentListBinding
import com.zgz.shoppingitems.shoppinglist.ShoppingListViewModel


class DeletedListFragment constructor(val listId : Long)  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_current_list, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ShoppingListDatabase.getInstance(application).shoppingItemDao

        val viewModelFactory = RunningShoppingListViewModelFactory(listId, dataSource, application)

        val shoppingListViewModel = ViewModelProvider(this, viewModelFactory).get(
            RunningShoppingListViewModel::class.java)
        binding.runningShoppingListViewModel = shoppingListViewModel
        binding.lifecycleOwner = this

        val adapter = RunningShoppingItemAdapter()
        binding.shoppingItemList.adapter = adapter


        val itemTouchHelper = ItemTouchHelper(ShoppingItemSwipeToDeleteCallBack(shoppingListViewModel, viewLifecycleOwner, adapter))
        itemTouchHelper.attachToRecyclerView(binding.shoppingItemList)


        shoppingListViewModel.deletedShoppingItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}