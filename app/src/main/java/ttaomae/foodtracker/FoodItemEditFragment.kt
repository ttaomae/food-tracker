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
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.databinding.FragmentFoodItemEditBinding
import ttaomae.foodtracker.viewmodel.FoodItemEditViewModel

@AndroidEntryPoint
class FoodItemEditFragment : Fragment(R.layout.fragment_food_item_edit) {
    private val args: FoodItemEditFragmentArgs by navArgs()
    private val viewModel: FoodItemEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setFoodItem(args.foodItemId)
        viewModel.setRestaurant(args.restaurantId)
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
        viewModel.foodItem.observe(viewLifecycleOwner) {
            binding.foodItem = it
        }
        viewModel.restaurant.observe(viewLifecycleOwner) {
            binding.restaurant = it
        }

        // After an item is saved, return to detail fragment.
        viewModel.savedItemId.observe(viewLifecycleOwner) {
            val navController = findNavController()
            // Depending on how we got to this fragment, use a different action so that the back
            // stack is put into the correct state.
            val previousDestinationId = navController.previousBackStackEntry?.destination?.id
            val action = if (previousDestinationId == R.id.foodItemDetailFragment) {
                FoodItemEditFragmentDirections.actionReturnToFoodItemDetail(it)
            } else {
                FoodItemEditFragmentDirections.actionGoToFoodItemDetail(it)
            }
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val restaurants = mutableListOf<Restaurant>()
        viewModel.restaurants.observe(viewLifecycleOwner) { result ->
            restaurants.addAll(result)
            val textLayout =
                view.findViewById<TextInputLayout>(R.id.text_input_layout_select_restaurant)
            val textView =
                view.findViewById<AutoCompleteTextView>(R.id.text_input_select_restaurant)
            textLayout.setEndIconOnClickListener { setupPopup(textLayout, restaurants, textView) }
            textView.setOnClickListener { setupPopup(textLayout, restaurants, textView) }
        }
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
            viewModel.selectRestaurant(restaurants[menuItem.order].id)
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
                saveFoodItem(requireView())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveFoodItem(view: View) {
        // Get field values and create new item.
        val nameView = view.findViewById<EditText>(R.id.text_input_food_item_name)
        val descriptionView = view.findViewById<EditText>(R.id.text_input_food_item_description)
        val ratingView = view.findViewById<RatingBar>(R.id.rating_bar_food_item_input)
        val commentsView = view.findViewById<EditText>(R.id.text_input_food_item_comments)
        val name = nameView.text.toString()
        val description = descriptionView.text.toStringOrNullIfBlank()
        val rating = ratingView.rating
        val comments = commentsView.text.toStringOrNullIfBlank()

        // Save to repository.
        viewModel.saveItem(name, description, rating, comments)
    }

    private fun CharSequence.toStringOrNullIfBlank(): String? {
        return if (this.isBlank()) {
            null
        } else {
            this.toString()
        }
    }
}
