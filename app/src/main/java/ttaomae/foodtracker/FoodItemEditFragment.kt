package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.databinding.FragmentFoodItemEditBinding
import javax.inject.Inject

@AndroidEntryPoint
class FoodItemEditFragment : Fragment(R.layout.fragment_food_item_edit) {
    @Inject lateinit var foodItemRepository: FoodItemRepository
    @Inject lateinit var restaurantRepository: RestaurantRepository
    private val args: FoodItemEditFragmentArgs by navArgs()
    private var foodItem: FoodItemWithRestaurant? = null
    private var restaurant: Restaurant? = null
    private lateinit var restaurants: List<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodItem = args.foodItem
        restaurant = foodItem?.restaurant

        runBlocking {
            launch {
                restaurants = restaurantRepository.getAll()
            }
        }
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
        view.findViewById<AutoCompleteTextView>(R.id.text_input_select_restaurant).apply {
            setOnClickListener { textInput ->
                val popup = PopupMenu(context, textInput)
                // When EditText is clicked, open a popup menu with all restaurant names.
                restaurants.forEachIndexed { index, restaurant ->
                    popup.menu.add(Menu.NONE, Menu.NONE, index, restaurant.name)
                }

                // When a popup menu item is clicked, set EditText value and update restaurant.
                popup.setOnMenuItemClickListener { menuItem ->
                    this.setText(menuItem.title)
                    restaurant = restaurants[menuItem.order]
                    true
                }
                popup.show()
            }
        }

        // Set save button behavior.
        view.findViewById<Button>(R.id.button_save_food_item).setOnClickListener {
            val savedItem = saveFoodItem(view)
            val action = FoodItemEditFragmentDirections.actionReturnToFoodItemDetail(savedItem)
            findNavController().navigate(action)
        }
    }

    private fun saveFoodItem(view: View): FoodItemWithRestaurant {
        // Get field values and create new item.
        val nameView = view.findViewById<EditText>(R.id.text_input_food_item_name)
        val descriptionView = view.findViewById<EditText>(R.id.text_input_food_item_description)
        val ratingView = view.findViewById<RatingBar>(R.id.rating_bar_food_item_input)
        val name = nameView.text.toString()
        val description = descriptionView.text.toString()
        val rating = ratingView.rating
        val foodItem = FoodItem(foodItem?.id, restaurant?.id, name, description, rating)
        var id: Long? = null

        // Save to repository.
        runBlocking {
            launch {
                id = foodItemRepository.save(foodItem)
            }
        }

        return FoodItemWithRestaurant(
            FoodItem(id!!, restaurant!!.id, name, description, rating),
            restaurant!!
        )
    }
}
