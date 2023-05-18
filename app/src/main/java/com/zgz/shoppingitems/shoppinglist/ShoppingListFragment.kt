package com.zgz.shoppingitems.shoppinglist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.zgz.shoppingitems.R
import com.zgz.shoppingitems.database.ShoppingListDatabase
import com.zgz.shoppingitems.databinding.FragmentShoppingListBinding

class ShoppingListFragment : Fragment() {

    //private lateinit var viewModel: ShoppingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentShoppingListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shopping_list, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = ShoppingListFragmentArgs.fromBundle(arguments!!)

        val dataSource = ShoppingListDatabase.getInstance(application).shoppingItemDao

        val listId = arguments.shoppingListId
        val viewModelFactory = ShoppingListViewModelFactory(listId, dataSource, application)

        val shoppingListViewModel =
            ViewModelProvider(this, viewModelFactory).get(ShoppingListViewModel::class.java)
        binding.shoppingListViewModel = shoppingListViewModel
        binding.lifecycleOwner = this

        val adapter = ShoppingItemAdapter(/*ShoppingItemSwipeDeleteListener {
            shoppingListViewModel.deleteShoppingItem(it)
//            Log.i("deleteSwipe", it.toString())
        }*/)
        binding.shoppingItemList.adapter = adapter

        binding.shoppingItemAmount.minValue = 1
        binding.shoppingItemAmount.maxValue = 10
        binding.shoppingItemAmount.value = 1

        binding.addNewButton.setOnClickListener {
            val checkedId = binding.importanceGroup.checkedRadioButtonId
            var priority = 0
            when(checkedId) {
               R.id.radio_vimp -> priority = 2
                R.id.radio_imp -> priority = 1
                else -> 0
            }
            shoppingListViewModel.insertNewShoppingItemToList(listId,
                binding.shoppingItemNameEdit.text.toString(),
                binding.shoppingItemAmount.value.toLong(),
                binding.shoppingItemUnitEdit.text.toString(),
                priority)

            binding.shoppingItemNameEdit.setText("")
            binding.shoppingItemNameEdit.requestFocus()
            binding.shoppingItemAmount.value = 1
            binding.shoppingItemUnitEdit.setText("")
            binding.radioNorm.isChecked = true
        }

        binding.saveListButton.setOnClickListener {
            val listName = binding.listTitleText.text.toString()
            shoppingListViewModel.updateShoppingListName(listId, listName)
            Log.i("update listname", "listid=" +id+ ",listname=" + listName)
            Toast.makeText(this.context, "Update List Name Successfully!", Toast.LENGTH_SHORT).show()

        }

        binding.runListButton.setOnClickListener {
            this.findNavController().navigate(
                ShoppingListFragmentDirections.actionShoppingListFragmentToRunningListHostFragment(listId)
            )
        }

        val itemTouchHelper = ItemTouchHelper(ShoppingItemSwipeToDeleteCallBack(shoppingListViewModel, viewLifecycleOwner, adapter))
        itemTouchHelper.attachToRecyclerView(binding.shoppingItemList)


        shoppingListViewModel.currentShoppingItems.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

/*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)
        // TODO: Use the ViewModel
    }
*/

}