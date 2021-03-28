package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.databinding.FoodItemSummaryBinding
import javax.inject.Inject

@AndroidEntryPoint
class ListFoodItemFragment : Fragment(R.layout.fragment_food_item_list) {
    @Inject lateinit var foodItemRepository: FoodItemRepository
    @Inject lateinit var restaurantRepository: RestaurantRepository
    lateinit var foodItems: List<FoodItemWithRestaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load items from repository.
        runBlocking {
            launch {
                foodItems =
                    restaurantRepository.getAllWithFoodItems().flatMap { it.asFoodItemList() }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodItemAdapter = FoodItemAdapter()
        foodItemAdapter.submitList(foodItems)

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
