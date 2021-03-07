package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import ttaomae.foodtracker.databinding.FragmentFoodItemDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class FoodItemDetailFragment : Fragment(R.layout.fragment_food_item_detail) {
    private var foodItem: FoodItem? = null
    @Inject lateinit var foodItemRepository: FoodItemRepository
    private val args: FoodItemDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load item from repository.
        runBlocking {
            launch {
                if (args.itemId != -1L) {
                    foodItem = foodItemRepository.get(args.itemId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentFoodItemDetailBinding>(
            layoutInflater, R.layout.fragment_food_item_detail, container, false)
        binding.isNew = foodItem?.id == null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update fields with values from item.
        view.findViewById<EditText>(R.id.edit_text_food_item_name).apply {
            setText(foodItem?.name)
        }
        view.findViewById<EditText>(R.id.edit_text_food_item_description).apply {
            setText(foodItem?.description)
        }
        view.findViewById<RatingBar>(R.id.rating_bar_food_item_input).apply {
            rating = foodItem?.rating ?: 0f
        }

        // Set add button behavior.
        view.findViewById<Button>(R.id.button_save_food_item).setOnClickListener {
            saveFoodItem(view)
            val action = FoodItemDetailFragmentDirections.actionReturnToFoodItemList()
            findNavController().navigate(action)
        }

        // Set delete button behavior.
        view.findViewById<Button>(R.id.button_delete_food_item).setOnClickListener {
            deleteFoodItem()
            val action = FoodItemDetailFragmentDirections.actionReturnToFoodItemList()
            findNavController().navigate(action)
        }
    }

    private fun saveFoodItem(view: View) {
        // Get field values and create new item.
        val nameView = view.findViewById<EditText>(R.id.edit_text_food_item_name)
        val descriptionView = view.findViewById<EditText>(R.id.edit_text_food_item_description)
        val ratingView = view.findViewById<RatingBar>(R.id.rating_bar_food_item_input)
        val foodItem = FoodItem(
            foodItem?.id,
            nameView.text.toString(),
            descriptionView.text.toString(),
            ratingView.rating
        )

        // Save to repository.
        runBlocking {
            launch {
                foodItemRepository.save(foodItem)
            }
        }
    }

    private fun deleteFoodItem() {
        foodItem?.let {
            runBlocking {
                launch {
                    foodItemRepository.delete(it)
                }
            }
        }
    }
}
