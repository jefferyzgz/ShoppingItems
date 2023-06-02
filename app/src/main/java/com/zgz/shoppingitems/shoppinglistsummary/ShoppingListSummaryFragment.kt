package com.zgz.shoppingitems.shoppinglistsummary

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.marginRight
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
            //Delete shopping list
            val builder = AlertDialog.Builder(this.requireContext(), R.style.AlertDialogTheme)

            builder.setMessage("Delete this list?")
                .setCancelable(true)
                .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                dialog, which -> deleteList(shoppingListSummaryViewModel, it)
            })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{
                    dialog, which -> Toast.makeText(this.context, "Delete Shopping List Cancelled!", Toast.LENGTH_SHORT).show()
                })
            val alert = builder.create()
            alert.setTitle("Delete List")// Shopping List")
            alert.show()

            //The text color of buttons need to be reset, otherwise it will be the same as the background color.
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this.requireContext(), R.color.white_text_color))
            //alert.getButton(AlertDialog.BUTTON_POSITIVE)
                //.setPadding(100, 0, 100, 0)

            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this.requireContext(), R.color.white_text_color))
            //alert.getButton(AlertDialog.BUTTON_NEGATIVE).marginRight

        }, ShoppingListSummarySelectListener {
            //Go to shopping list
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

    fun deleteList(shoppingListSummaryViewModel : ShoppingListSummaryViewModel, listId : Long) {
        shoppingListSummaryViewModel.onDeleteList(listId)
        Toast.makeText(this.context, "Delete Shopping List Successfully!", Toast.LENGTH_SHORT).show()
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