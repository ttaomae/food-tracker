package ttaomae.foodtracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.databinding.FoodItemSummaryBinding
import ttaomae.foodtracker.viewmodel.FoodItemListViewModel

@AndroidEntryPoint
class FoodItemListFragment : Fragment(R.layout.fragment_food_item_list) {
    private val viewModel: FoodItemListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodItemAdapter = FoodItemAdapter { inflater, parent ->
            val binding = FoodItemSummaryBinding.inflate(inflater, parent, false)
            FoodItemSummaryViewHolder(binding)
        }

        viewModel.foodItems.observe(viewLifecycleOwner) { result ->
            foodItemAdapter.submitList(result)
        }

        // Setup RecyclerView
        view.findViewById<RecyclerView>(R.id.recycler_view_items_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = foodItemAdapter
        }

        // Set add button behavior.
        view.findViewById<FloatingActionButton>(R.id.fab_add_item).setOnClickListener {
            val action = MainFragmentDirections.actionAddFoodItem()
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
    }
}

class FoodItemSummaryViewHolder(binding: FoodItemSummaryBinding) :
    FoodItemViewHolder<FoodItemSummaryBinding>(binding) {
    override fun bind(foodItem: FoodItemWithRestaurant) {
        binding.foodItem = foodItem
        binding.executePendingBindings()
    }

    override fun onClick(view: View) {
        binding.foodItem?.let {
            val action = MainFragmentDirections.actionViewFoodItem(it.foodItem.id)
            itemView.findNavController().navigate(action)
        }
    }
}
