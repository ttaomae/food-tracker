package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.databinding.FragmentRestaurantDetailBinding
import ttaomae.foodtracker.databinding.RestaurantItemSummaryBinding
import ttaomae.foodtracker.viewmodel.RestaurantDetailViewModel

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment(R.layout.fragment_restaurant_detail) {
    private val args: RestaurantDetailFragmentArgs by navArgs()
    private val viewModel: RestaurantDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setRestaurant(args.restaurantId)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentRestaurantDetailBinding>(
            layoutInflater, R.layout.fragment_restaurant_detail, container, false
        )
        viewModel.restaurant.observe(viewLifecycleOwner) {
            binding.restaurant = it.restaurant
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodItemAdapter = FoodItemAdapter { inflater, parent ->
            val binding = RestaurantItemSummaryBinding.inflate(inflater, parent, false)
            RestaurantItemViewHolder(binding)
        }

        viewModel.restaurant.observe(viewLifecycleOwner) { result ->
            val items = result.asFoodItemList()
            foodItemAdapter.submitList(items)
        }

        // Setup RecyclerView
        view.findViewById<RecyclerView>(R.id.recycler_view_restaurant_items_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodItemAdapter
        }

        view.findViewById<FloatingActionButton>(R.id.fab_add_restaurant_item).setOnClickListener {
            viewModel.restaurant.observe(viewLifecycleOwner) {
                val action = RestaurantDetailFragmentDirections.actionAddRestaurantItem(
                    restaurantId = it.restaurant.id
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_edit -> {
                viewModel.restaurant.observe(viewLifecycleOwner) {
                    val id = it.restaurant.id
                    val action = RestaurantDetailFragmentDirections.actionEditRestaurant(id)
                    findNavController().navigate(action)
                }
                true
            }
            R.id.menu_item_delete -> {
                deleteRestaurant()
                val action = RestaurantDetailFragmentDirections
                    .actionReturnToRestaurantList(MainFragment.RESTAURANT_TAB_INDEX)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteRestaurant() {
        viewModel.deleteRestaurant()
    }
}

class RestaurantItemViewHolder(binding: RestaurantItemSummaryBinding) :
    FoodItemViewHolder<RestaurantItemSummaryBinding>(binding) {
    override fun bind(foodItem: FoodItemWithRestaurant) {
        binding.foodItem = foodItem
        binding.executePendingBindings()
    }

    override fun onClick(view: View) {
        binding.foodItem?.let {
            val id = it.foodItem.id
            val action = RestaurantDetailFragmentDirections.actionEditRestaurantItem(id)
            itemView.findNavController().navigate(action)
        }
    }
}
