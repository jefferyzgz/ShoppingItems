package com.zgz.shoppingitems.runninglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.zgz.shoppingitems.R
import com.zgz.shoppingitems.database.ShoppingListDatabase
import com.zgz.shoppingitems.databinding.FragmentRunningListHostBinding
import com.zgz.shoppingitems.shoppinglist.ShoppingListFragmentArgs
import kotlinx.android.synthetic.main.fragment_running_list_host.*

/**
 * A simple [Fragment] subclass.
 * Use the [RunningListHostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunningListHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRunningListHostBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_running_list_host, container, false
        )

//        val arguments = ShoppingListFragmentArgs.fromBundle(requireArguments())
        val listId = requireArguments().getLong("shoppingListId")
//        this.arguments
//        requireArguments().putLong("listId", listId)

        val fragmentList = listOf(
            BoughtListFragment.newInstance(listId),
            CurrentListFragment.newInstance(listId),
            DeletedListFragment.newInstance(listId)
        )

        val application = requireNotNull(this.activity).application
        val dataSource = ShoppingListDatabase.getInstance(application).shoppingItemDao

        val viewModelFactory = RunningShoppingListViewModelFactory(listId, dataSource, application)

        val shoppingListViewModel = ViewModelProvider(this, viewModelFactory).get(
            RunningShoppingListViewModel::class.java)
        binding.runningShoppingListViewModel = shoppingListViewModel

//        val fragmentList = shoppingListViewModel.fragmentList

        val viewPagerAdapter = RunningListPagerAdapter(childFragmentManager, lifecycle, fragmentList)
        val viewPager2 = binding.runningListPager

        viewPager2.adapter = viewPagerAdapter


        val bottomNav = binding.bottomNavigationView

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position){
                    0 -> bottomNav.selectedItemId = R.id.bought_list
                    1 -> bottomNav.selectedItemId = R.id.current_list
                    2 -> bottomNav.selectedItemId = R.id.deleted_list
                }
                super.onPageSelected(position)
            }
        })

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bought_list -> {
                    viewPager2.currentItem = 0
                    true
                }
                R.id.current_list -> {
                    viewPager2.currentItem = 1
                    true
                }
                R.id.deleted_list -> {
                    viewPager2.currentItem = 2
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.current_list

/*        val dataSource = ShoppingListDatabase.getInstance(application).shoppingItemDao

        val viewModelFactory = ShoppingListViewModelFactory(listId, dataSource, application)

        val shoppingListViewModel =
            ViewModelProvider(this, viewModelFactory).get(ShoppingListViewModel::class.java)
*/
        //binding.shoppingListSummaryViewModel = shoppingListSummaryViewModel

        binding.lifecycleOwner = this

        return binding.root
    }

}