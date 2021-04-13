package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.databinding.FragmentFoodItemEditBinding
import ttaomae.foodtracker.viewmodel.RestaurantListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FoodItemEditFragment : Fragment(R.layout.fragment_food_item_edit) {
    @Inject lateinit var foodItemRepository: FoodItemRepository
    private val args: FoodItemEditFragmentArgs by navArgs()
    private var foodItem: FoodItemWithRestaurant? = null
    private var restaurantId: Long? = null
    private val viewModel: RestaurantListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodItem = args.foodItem
        restaurantId = foodItem?.restaurant?.id
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentFoodItemEditBinding>(
            layoutInflater, R.layout.fragment_food_item_edit, container, false
        )
        binding.foodItem = foodItem
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val restaurants = mutableListOf<Restaurant>()
        viewModel.restaurants.observe(viewLifecycleOwner) { result ->
            result.forEach { restaurants.add(it) }
        }

        val textLayout =
            view.findViewById<TextInputLayout>(R.id.text_input_layout_select_restaurant)
        val textView = view.findViewById<AutoCompleteTextView>(R.id.text_input_select_restaurant)
        textLayout.setEndIconOnClickListener { setupPopup(textLayout, restaurants, textView) }
        textView.setOnClickListener { setupPopup(textLayout, restaurants, textView) }
    }

    private fun setupPopup(
        anchor: View,
        restaurants: List<Restaurant>,
        textView: AutoCompleteTextView
    ) {
        val popup = PopupMenu(context, anchor)

        // Add all restaurants to list.
        restaurants.forEachIndexed { index, restaurant ->
            popup.menu.add(Menu.NONE, Menu.NONE, index, restaurant.name)
        }

        // When a popup menu item is clicked, set EditText value and update restaurant.
        popup.setOnMenuItemClickListener { menuItem ->
            textView.setText(menuItem.title)
            restaurantId = restaurants[menuItem.order].id
            println(restaurantId)
            true
        }
        popup.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_save -> {
                val savedItem = saveFoodItem(requireView())
                val action = FoodItemEditFragmentDirections.actionReturnToFoodItemDetail(savedItem)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveFoodItem(view: View): FoodItemWithRestaurant {
        // Get field values and create new item.
        val restaurantView =
            view.findViewById<AutoCompleteTextView>(R.id.text_input_select_restaurant)
        val nameView = view.findViewById<EditText>(R.id.text_input_food_item_name)
        val descriptionView = view.findViewById<EditText>(R.id.text_input_food_item_description)
        val ratingView = view.findViewById<RatingBar>(R.id.rating_bar_food_item_input)
        val restaurantName = restaurantView.text.toString()
        val name = nameView.text.toString()
        val description = descriptionView.text.toString()
        val rating = ratingView.rating
        val foodItem = FoodItem(foodItem?.id, restaurantId, name, description, rating)
        var id: Long? = null

        // Save to repository.
        runBlocking {
            launch {
                id = foodItemRepository.save(foodItem)
            }
        }

        return FoodItemWithRestaurant(
            FoodItem(id!!, restaurantId, name, description, rating),
            Restaurant(restaurantId, restaurantName)
        )
    }
}
