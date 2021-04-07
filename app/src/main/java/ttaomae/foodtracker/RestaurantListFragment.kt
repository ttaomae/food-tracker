package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
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
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.databinding.RestaurantSummaryBinding
import ttaomae.foodtracker.viewmodel.RestaurantListViewModel

@AndroidEntryPoint
class ListRestaurantFragment : Fragment(R.layout.fragment_restaurant_list) {
    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val restaurantAdapter = RestaurantAdapter()
        viewModel.restaurants.observe(viewLifecycleOwner) { result ->
            restaurantAdapter.submitList(result)
        }

        // Setup RecyclerView.
        view.findViewById<RecyclerView>(R.id.recycler_view_restaurants_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantAdapter
        }

        // Set add button behavior.
        view.findViewById<FloatingActionButton>(R.id.fab_add_restaurant).setOnClickListener {
            val action = ListRestaurantFragmentDirections.actionAddRestaurant()
            findNavController().navigate(action)
        }
    }
}

class RestaurantAdapter :
    ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantCallback) {
    class ViewHolder(private val binding: RestaurantSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                binding.restaurant?.let {
                    val action = ListRestaurantFragmentDirections.actionLoadRestaurantDetails(it)
                    binding.root.findNavController().navigate(action)
                }
            }
        }

        fun bind(restaurant: Restaurant) {
            binding.restaurant = restaurant
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RestaurantSummaryBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.bind(restaurant)
    }
}

object RestaurantCallback : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.name == newItem.name
    }
}
