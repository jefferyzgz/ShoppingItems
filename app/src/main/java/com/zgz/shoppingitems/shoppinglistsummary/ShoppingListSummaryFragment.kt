package com.zgz.shoppingitems.shoppinglistsummary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zgz.shoppingitems.R
import com.zgz.shoppingitems.database.ShoppingListDatabase
import com.zgz.shoppingitems.databinding.FragmentShoppingListSummaryBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingListSummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingListSummaryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View?{
        val binding: FragmentShoppingListSummaryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shopping_list_summary, container, false
        )

        val application = requireNotNull(this.activity).application

        val dataSource = ShoppingListDatabase.getInstance(application).shoppingItemDao

        val viewModelFactory = ShoppingListSummaryViewModelFactory(dataSource, application)

        val shoppingListSummaryViewModel = ViewModelProvider(
            this, viewModelFactory).get(ShoppingListSummaryViewModel::class.java)

        binding.shoppingListSummaryViewModel = shoppingListSummaryViewModel

        binding.lifecycleOwner = this

        //
        val adapter = ShoppingListSummaryAdapter(ShoppingListSummaryDeleteListener {
            shoppingListSummaryViewModel.onDeleteList(it)
            Toast.makeText(this.context, "Delete Shopping List Successfully!", Toast.LENGTH_SHORT).show()
        }, ShoppingListSummarySelectListener {
            shoppingListSummaryViewModel.onNavigateToList(it)
        })

        binding.shoppingListSummaryList.adapter = adapter

        shoppingListSummaryViewModel.shoppingListSummary.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }

        })

        shoppingListSummaryViewModel.navigateToNewList.observe(viewLifecycleOwner, { id ->
            id?.let {
                //Log.i("insertList nav---", it.toString())
                this.findNavController().navigate(
                    ShoppingListSummaryFragmentDirections.actionShoppingListSummaryFragmentToShoppingListFragment(it)
                )
                shoppingListSummaryViewModel.doneNavigating()
            }
        })

        return binding.root
    }

/*
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShoppingListSummaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShoppingListSummaryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }*/
}