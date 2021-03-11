package ttaomae.foodtracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.databinding.FragmentRestaurantDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantDetailFragment : Fragment(R.layout.fragment_restaurant_detail) {
    private var restaurant: Restaurant? = null
    @Inject lateinit var restaurantRepository: RestaurantRepository
    private val args: RestaurantDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load restaurant from repository
        runBlocking {
            launch {
                if (args.restaurantId != -1L) {
                    restaurant = restaurantRepository.get(args.restaurantId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentRestaurantDetailBinding>(
            layoutInflater, R.layout.fragment_restaurant_detail, container, false)
        Log.d("RestaurantDetail", "${restaurant?.id}")
        binding.isNew = restaurant?.id == null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update field with value from restaurant.
        view.findViewById<EditText>(R.id.edit_text_restaurant_name).apply {
            setText(restaurant?.name)
        }

        // Set add button behavior.
        view.findViewById<Button>(R.id.button_save_restaurant).setOnClickListener {
            saveRestaurant(view)
            val action = RestaurantDetailFragmentDirections.actionReturnToRestaurantList()
            findNavController().navigate(action)
        }

        // Set delete button behavior.
        view.findViewById<Button>(R.id.button_delete_restaurant).setOnClickListener {
            deleteRestaurant()
            val action = RestaurantDetailFragmentDirections.actionReturnToRestaurantList()
            findNavController().navigate(action)
        }
    }

    private fun saveRestaurant(view: View) {
        val name = view.findViewById<EditText>(R.id.edit_text_restaurant_name).text.toString()
        val restaurant = Restaurant(restaurant?.id, name)

        runBlocking {
            launch {
                restaurantRepository.save(restaurant)
            }
        }
    }

    private fun deleteRestaurant() {
        restaurant?.let {
            runBlocking {
                launch {
                    restaurantRepository.delete(it)
                }
            }
        }
    }
}