package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import javax.inject.Inject

@AndroidEntryPoint
class ListFoodItemFragment : Fragment(R.layout.fragment_food_item_list) {
    @Inject lateinit var foodItemRepository: FoodItemRepository
    lateinit var foodItems: List<FoodItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load items from repository.
        runBlocking {
            launch {
                foodItems  = foodItemRepository.getAll()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val foodItemAdapter = FoodItemAdapter()
        foodItemAdapter.submitList(foodItems)

        // Setup RecyclerView
        view.findViewById<RecyclerView>(R.id.recycler_view_items_list).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = foodItemAdapter
        }

        // Set add button behavior.
        view.findViewById<FloatingActionButton>(R.id.fab_add_item).setOnClickListener {
            val action = ListFoodItemFragmentDirections.actionLoadFoodItemDetails()
            findNavController().navigate(action)
        }
    }
}

class FoodItemAdapter : ListAdapter<FoodItem, FoodItemAdapter.ViewHolder>(FoodItemCallback) {
    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val nameView: TextView = view.findViewById(R.id.text_view_item_name)
        private val descriptionView: TextView = view.findViewById(R.id.text_view_item_description)
        private val ratingBar: RatingBar = view.findViewById(R.id.rating_bar_item_display)
        private var item: FoodItem? = null

        init {
            view.setOnClickListener {
                item?.id?.let {
                    val action = ListFoodItemFragmentDirections.actionLoadFoodItemDetails(it)
                    view.findNavController().navigate(action)
                }
            }
        }

        fun bind(foodItem: FoodItem) {
            nameView.text = foodItem.name
            descriptionView.text = foodItem.description
            ratingBar.rating = foodItem.rating
            this.item = foodItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = getItem(position)
        holder.bind(foodItem)
    }
}

object FoodItemCallback : DiffUtil.ItemCallback<FoodItem>() {
    override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
        return oldItem.name == newItem.name
                && oldItem.description == newItem.description
                && oldItem.rating == newItem.rating
    }
}
