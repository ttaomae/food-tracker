package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import javax.inject.Inject

@AndroidEntryPoint
class ListRestaurantFragment : Fragment(R.layout.fragment_restaurant_list) {
    @Inject lateinit var restaurantRepository: RestaurantRepository
    lateinit var restaurants: List<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load restaurants from repository.
        runBlocking {
            launch {
                restaurants = restaurantRepository.getAll()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val restaurantAdapter = RestaurantAdapter()
        restaurantAdapter.submitList(restaurants)


        // Setup RecyclerView.
        view.findViewById<RecyclerView>(R.id.recycler_view_restaurants_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantAdapter
        }

        // Set add button behavior.
        view.findViewById<FloatingActionButton>(R.id.fab_add_restaurant).setOnClickListener {
            val action = ListRestaurantFragmentDirections.actionLoadRestaurantDetails()
            findNavController().navigate(action)
        }
    }
}

class RestaurantAdapter :
    ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantCallback) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameView: TextView = view.findViewById(R.id.text_view_restaurant_name)
        private var restaurant: Restaurant? = null

        init {
            view.setOnClickListener {
                restaurant?.id?.let {
                    val action = ListRestaurantFragmentDirections.actionLoadRestaurantDetails(it)
                    view.findNavController().navigate(action)
                }
            }
        }

        fun bind(restaurant: Restaurant) {
            nameView.text = restaurant.name
            this.restaurant = restaurant
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant, parent, false)
        return ViewHolder(view)
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
