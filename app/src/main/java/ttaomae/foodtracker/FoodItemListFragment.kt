package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.databinding.FoodItemSummaryBinding
import ttaomae.foodtracker.viewmodel.FoodItemListViewModel

@AndroidEntryPoint
class ListFoodItemFragment : Fragment(R.layout.fragment_food_item_list) {
    private val viewModel: FoodItemListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodItemAdapter = FoodItemAdapter()
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
            val action = ListFoodItemFragmentDirections.actionAddFoodItem()
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
    }
}

class FoodItemAdapter :
    ListAdapter<FoodItemWithRestaurant, FoodItemAdapter.ViewHolder>(FoodItemCallback) {
    class ViewHolder(private val binding: FoodItemSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.foodItem?.let {
                    val action = ListFoodItemFragmentDirections.actionLoadFoodItemDetails(it)
                    binding.root.findNavController().navigate(action)
                }
            }
        }

        fun bind(foodItem: FoodItemWithRestaurant) {
            binding.foodItem = foodItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FoodItemSummaryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = getItem(position)
        holder.bind(foodItem)
    }
}

object FoodItemCallback : DiffUtil.ItemCallback<FoodItemWithRestaurant>() {
    override fun areItemsTheSame(
        oldItem: FoodItemWithRestaurant,
        newItem: FoodItemWithRestaurant
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: FoodItemWithRestaurant,
        newItem: FoodItemWithRestaurant
    ): Boolean {
        return oldItem.foodItem.name == newItem.name
                && oldItem.description == newItem.description
                && oldItem.rating == newItem.rating
                && oldItem.restaurant.name == newItem.restaurant.name
    }
}
