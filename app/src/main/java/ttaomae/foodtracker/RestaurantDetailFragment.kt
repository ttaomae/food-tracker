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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.databinding.FragmentRestaurantDetailBinding
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
            binding.restaurant = it
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_edit -> {
                viewModel.restaurant.observe(viewLifecycleOwner) {
                    val action = RestaurantDetailFragmentDirections.actionEditRestaurant(it.id)
                    findNavController().navigate(action)
                }
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
        viewModel.deleteRestaurant()
    }
}
