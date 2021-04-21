package ttaomae.foodtracker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.databinding.FoodItemSummaryBinding
import ttaomae.foodtracker.viewmodel.FoodItemListViewModel

@AndroidEntryPoint
class FoodItemListFragment : Fragment(R.layout.fragment_food_item_list) {
    private val viewModel: FoodItemListViewModel by viewModels()

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
