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
import ttaomae.foodtracker.data.FoodItemRepository
import ttaomae.foodtracker.data.FoodItemWithRestaurant
import ttaomae.foodtracker.databinding.FragmentFoodItemDetailBinding
import javax.inject.Inject

@AndroidEntryPoint
class FoodItemDetailFragment : Fragment(R.layout.fragment_food_item_detail) {
    @Inject lateinit var foodItemRepository: FoodItemRepository
    private val args: FoodItemDetailFragmentArgs by navArgs()
    private lateinit var foodItem: FoodItemWithRestaurant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodItem = args.foodItem
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentFoodItemDetailBinding>(
            layoutInflater, R.layout.fragment_food_item_detail, container, false
        )
        binding.foodItem = foodItem
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_edit -> {
                val action = FoodItemDetailFragmentDirections.actionEditFoodItem(foodItem)
                findNavController().navigate(action)
                true
            }
            R.id.menu_item_delete -> {
                deleteFoodItem()
                val action = FoodItemDetailFragmentDirections.actionReturnToFoodItemList()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteFoodItem() {
        foodItem.foodItem.let {
            runBlocking {
                launch {
                    foodItemRepository.delete(it)
                }
            }
        }
    }
}
