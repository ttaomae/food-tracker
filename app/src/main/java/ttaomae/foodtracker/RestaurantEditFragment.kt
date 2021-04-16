package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.databinding.FragmentRestaurantEditBinding
import ttaomae.foodtracker.viewmodel.RestaurantEditViewModel

@AndroidEntryPoint
class RestaurantEditFragment : Fragment(R.layout.fragment_restaurant_edit) {
    private val args: RestaurantEditFragmentArgs by navArgs()
    private val viewModel: RestaurantEditViewModel by viewModels()

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
        val binding = DataBindingUtil.inflate<FragmentRestaurantEditBinding>(
            layoutInflater, R.layout.fragment_restaurant_edit, container, false
        )
        viewModel.restaurant.observe(viewLifecycleOwner) {
            binding.restaurant = it
        }

        // After a restaurant is saved, return to detail fragment.
        viewModel.savedRestaurantId.observe(viewLifecycleOwner) {
            val action = RestaurantEditFragmentDirections.actionReturnToRestaurantDetail(it)
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_save -> {
                saveRestaurant(requireView())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveRestaurant(view: View) {
        val name = view.findViewById<EditText>(R.id.text_input_restaurant_name).text.toString()
        viewModel.saveRestaurant(name)

    }
}
