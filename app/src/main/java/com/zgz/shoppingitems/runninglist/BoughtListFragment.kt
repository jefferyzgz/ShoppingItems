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
import com.zgz.shoppingitems.databinding.FragmentBoughtListBinding
import com.zgz.shoppingitems.shoppinglist.*


class BoughtListFragment constructor(val listId : Long) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentBoughtListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_bought_list, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = ShoppingListDatabase.getInstance(application).shoppingItemDao

        val viewModelFactory = RunningShoppingListViewModelFactory(listId, dataSource, application)

        val shoppingListViewModel = ViewModelProvider(this, viewModelFactory).get(RunningShoppingListViewModel::class.java)
        binding.runningShoppingListViewModel = shoppingListViewModel
        binding.lifecycleOwner = this

        val adapter = RunningShoppingItemAdapter()
        binding.shoppingItemList.adapter = adapter


        val itemTouchHelper = ItemTouchHelper(ShoppingItemSwipeToDeleteCallBack(shoppingListViewModel, viewLifecycleOwner, adapter))
        itemTouchHelper.attachToRecyclerView(binding.shoppingItemList)


        shoppingListViewModel.boughtShoppingItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}