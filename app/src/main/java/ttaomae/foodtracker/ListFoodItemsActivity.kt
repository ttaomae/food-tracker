package ttaomae.foodtracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.FoodItem
import ttaomae.foodtracker.data.FoodItemRepository
import javax.inject.Inject

@AndroidEntryPoint
class ListFoodItemsActivity : AppCompatActivity() {
    @Inject lateinit var foodItemRepository: FoodItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_food_items)

        val linearLayoutManager = LinearLayoutManager(this)
        val foodItemAdapter = FoodItemAdapter { startAddFoodItemActivity(it) }
        runBlocking {
            launch {
                val food = foodItemRepository.getAll()
                foodItemAdapter.submitList(food)
            }
        }
        findViewById<RecyclerView>(R.id.itemsList).apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = foodItemAdapter
        }
    }

    fun addFoodItem(view: View) {
        startAddFoodItemActivity(null)
    }

    private fun startAddFoodItemActivity(item: FoodItem?) {
        val intent = Intent(this, AddFoodItemActivity::class.java)
        item?.apply { intent.putExtra("itemId", item.id) }
        startActivity(intent)
    }
}

class FoodItemAdapter(private val onClick: (FoodItem) -> Unit) :
    ListAdapter<FoodItem, FoodItemAdapter.ViewHolder>(FoodItemCallback) {

    class ViewHolder(view: View, private val onClick: (FoodItem) -> Unit) :
        RecyclerView.ViewHolder(view) {

        val nameView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionView: TextView = view.findViewById(R.id.descriptionTextView)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarDisplay)
        var item: FoodItem? = null

        init {
            view.setOnClickListener {
                item?.let {
                    onClick(it)
                }
            }
        }

        fun bind(item: FoodItem) {
            nameView.text = item.name
            descriptionView.text = item.description
            ratingBar.rating = item.rating
            this.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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
