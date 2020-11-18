package ttaomae.foodtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ttaomae.foodtracker.model.FoodItem


class ListFoodItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_food_items)

        val items = (application as FoodTrackerApplication).foodItems
        println(items)

        val linearLayoutManager = LinearLayoutManager(this)
        val foodItemAdapter = FoodItemAdapter()
        foodItemAdapter.submitList(items)
        findViewById<RecyclerView>(R.id.itemsList).apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = foodItemAdapter
        }

    }

    fun addFoodItem(view: View) {
        val intent = Intent(this, AddFoodItemActivity::class.java)
        startActivity(intent)
    }
}

class FoodItemAdapter() :
    ListAdapter<FoodItem, FoodItemAdapter.ViewHolder>(FoodItemCallback) {

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val nameView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionView: TextView = view.findViewById(R.id.descriptionTextView)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarDisplay)
        var item: FoodItem? = null

        fun bind(item: FoodItem) {
            nameView.text = item.name
            descriptionView.text = item.description
            ratingBar.rating = item.rating
            this.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view)
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
        return oldItem.name == newItem.description
                && oldItem.description == newItem.description
                && oldItem.rating == newItem.rating
    }
}
