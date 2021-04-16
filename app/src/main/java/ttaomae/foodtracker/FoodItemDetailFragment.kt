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
import ttaomae.foodtracker.databinding.FragmentFoodItemDetailBinding
import ttaomae.foodtracker.viewmodel.FoodItemDetailViewModel

@AndroidEntryPoint
class FoodItemDetailFragment : Fragment(R.layout.fragment_food_item_detail) {
    private val args: FoodItemDetailFragmentArgs by navArgs()
    private val viewModel: FoodItemDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setItem(args.foodItemId)
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
        viewModel.foodItem.observe(viewLifecycleOwner) {
            binding.foodItem = it
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_edit -> {
                viewModel.foodItem.observe(viewLifecycleOwner) {
                    val action = FoodItemDetailFragmentDirections.actionEditFoodItem(it.foodItem.id)
                    findNavController().navigate(action)
                }
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
        viewModel.deleteItem()
    }
}
