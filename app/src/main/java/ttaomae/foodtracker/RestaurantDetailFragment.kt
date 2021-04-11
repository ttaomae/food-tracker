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
    @Inject lateinit var restaurantRepository: RestaurantRepository
    private val args: RestaurantDetailFragmentArgs by navArgs()
    private lateinit var restaurant: Restaurant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurant = args.restaurant
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
        binding.restaurant = restaurant
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_edit -> {
                val action = RestaurantDetailFragmentDirections.actionEditRestaurant(restaurant)
                findNavController().navigate(action)
                true
            }
            R.id.menu_item_delete -> {
                deleteRestaurant()
                val action = RestaurantDetailFragmentDirections.actionReturnToRestaurantList()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteRestaurant() {
        restaurant.let {
            runBlocking {
                launch {
                    restaurantRepository.delete(it)
                }
            }
        }
    }
}
